/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkrbl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author utente
 */
public class CheckRBL implements callbackPrint {

    /**
     * Map of the results; key = resource as specified by arguments or generated
     * value= Provider class
     */
    static Map<String, List<Provider>> resultDB = new HashMap<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CheckRBL checkRBL = new CheckRBL();
        Options options = new Options();
        options.addOption("c", false, "Check domini. Opposto al flag 'p'\nDa usare con gli altri flag altrimenti non fa nulla");
        options.addOption("p", false, "Stampa i domini. Opposto al flag 'c'\nDa usare con gli altri flag altrimenti non fa nulla");
        options.addOption(Option.builder("a").desc("Indirizzi IP (separati da virgole) da verificare")
                .hasArgs().valueSeparator(',').build());
        options.addOption(Option.builder("d").desc("Domini (separati da virgole) da verificare")
                .hasArgs().valueSeparator(',').build());
        options.addOption("f", true, "File con i domini da verificare");
        options.addOption(Option.builder("g").desc("Genera domini. Deve essere usato con le opzioni 'c' o 'p'\n Arg: counter_start_value,counter_end_value,counterPrintfFormat,prefix,suffix\n"
                + "Es: -g 1,9,02,goofy,.mouse")
                .hasArgs().valueSeparator(',').numberOfArgs(5).build());

        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = commandLineParser.parse(options, args);
        } catch (ParseException ex) {
            printHelp(options);
            exit(0);
//
//            Logger.getLogger(CheckRBL.class.getName()).log(Level.SEVERE, null, ex);
        }

        Option[] optionArray = cmd.getOptions();

        List<String> resources = new ArrayList<>();

        if ((!cmd.hasOption("c") && !cmd.hasOption("p")) || (cmd.hasOption("c") && cmd.hasOption("p"))) {
            printHelp(options);
            exit(0);
        }

        for (Option optionArray1 : optionArray) {
            switch (optionArray1.getId()) {
                case 'a':
                    resources.addAll(Arrays.asList(cmd.getOptionValues("a")));
                    break;
                case 't':
                    break;
                case 'f':
                    resources.addAll(readResourcesFromFile(cmd.getOptionValue("f")));
                    break;
                case 'd':
                    resources.addAll(Arrays.asList(cmd.getOptionValues("d")));
                    break;
                case 'g':
                    List<String> domains = Arrays.asList(cmd.getOptionValues("g"));

                    int start = Integer.parseInt(domains.get(0));
                    int end = Integer.parseInt(domains.get(1));
                    String format = domains.get(2);
                    String prefix = domains.get(3);
                    String suffix = domains.get(4);
                    resources.addAll(checkRBL.generateDomainFromCounter(start, end, prefix, suffix, format));

                    break;
                case 'p':
                    break;
                case 'c':
                    break;
                default:
                    HelpFormatter formatter = new HelpFormatter();
                    formatter.printHelp("CheckRBL", options);
                    exit(0);
            }
        }

        CheckSpamhaus checkSpamhaus = new CheckSpamhaus("Spamhaus");
        CheckSpameatingmonkey checkSpameatingmonkey = new CheckSpameatingmonkey("Spameatingmonkey");

        List<String> resultsSpamhaus = null;
        List<String> resultsSpameatingmonkey = null;
        if (cmd.hasOption('c')) {
            System.out.println("---------- INIZIO VERIFICA ----------");
            resultsSpamhaus = checkSpamhaus.checkResources(resources, checkRBL);
            resultsSpameatingmonkey = checkSpameatingmonkey.checkResources(resources, checkRBL);
            System.out.println("-----------  FINE VERIFICA  ---------");
        }

        System.out.println("---------- RISULTATI ----------");
        String dbKey;
        Provider provider;
        List<Provider> providers = null;
        for (int j = 0; j < resources.size(); j++) {
            dbKey = resources.get(j);
            //System.out.println(String.format("%s", resources.get(j)));
            if (cmd.hasOption('c')) {
                providers = new ArrayList<>();
                provider = new Provider("Spamhaus", resultsSpamhaus.get(j),
                        checkSpamhaus.convertResultCode(resultsSpamhaus.get(j))
                        );
                providers.add(provider);
//                System.out.println(String.format("\tspamhaus: %s - %s",
//                        resultsSpamhaus.get(j),
//                        checkSpamhaus.convertResultCode(resultsSpamhaus.get(j))));

                provider = new Provider("Spameatingmonkey",
                        resultsSpameatingmonkey.get(j),
                        checkSpameatingmonkey.convertResultCode(resultsSpameatingmonkey.get(j))
                        );
                providers.add(provider);

//                System.out.println(String.format("\tspamEatingMonkey: %s - %s",
//                        resultsSpameatingmonkey.get(j),
//                        checkSpameatingmonkey.convertResultCode(resultsSpameatingmonkey.get(j))));
            } else {
                //   System.out.println("");
            }

            resultDB.put(dbKey, providers);
        }

        printResultDB();
    }

    private static void printResultDB() {
        List<Provider> providers = null;
        for (String key : resultDB.keySet()) {
            providers = resultDB.get(key);
            System.out.println(key);
            for (Provider provider : providers) {
                System.out.println(String.format("\tProvider: %s - Result code: %s (%s)",
                        provider.providerName, provider.resultCode, provider.resultDesctiption)
                );
            }
        }

    }

    private static List<String> readResourcesFromFile(String filename) {
        File file = new File(filename);

        BufferedReader bis = null;
        String resource2Check;
        List<String> resources = new ArrayList<>();
        try {
            //bis = new BufferedReader(new FileReader(file));
            bis = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            while ((resource2Check = bis.readLine()) != null) {
                if (resource2Check.trim().length() > 0) {
                    resources.add(resource2Check);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CheckRBL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CheckRBL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(CheckRBL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return resources;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CheckRBL", options);
    }

    /**
     * Generate a list o domain to be checked with a counter within
     *
     * @param from start counter
     * @param to end counter (including value)
     * @param prefix prefix string of built domain
     * @param suffix suffix string of built domain
     * @param printfFormat format of the printed counter in printf format (es.
     * 02 will generate a counter with 2 numbers padded with 0 example
     * generateDomainFromCounter(0,10,"goofy",".mouse","02) will generate a
     * domain list as follows: goofy00.mouse goofy01.mouse ... goofy10.mouse
     *
     * @return The list of the domain generated
     */
    public List<String> generateDomainFromCounter(int from, int to, String prefix, String suffix, String printfFormat) {
        List<String> temp = new ArrayList<>();
        String format = "%s%" + printfFormat + "d%s";
        for (int i = from; i <= to; i++) {
            temp.add(String.format(format, prefix, i, suffix));
        }
        return temp;
    }

    /**
     * The callback displaying the working process
     *
     * @param value
     */
    @Override
    public void doAction(String value) {
        System.out.println(value);
    }

}
