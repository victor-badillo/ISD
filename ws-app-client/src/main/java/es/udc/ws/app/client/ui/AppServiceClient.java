package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientGameService;
import es.udc.ws.app.client.service.ClientGameServiceFactory;
import es.udc.ws.app.client.service.dto.ClientGameDto;
import es.udc.ws.app.client.service.exceptions.ClientGameAlreadyPlayedException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.app.client.service.exceptions.ClientNotValidCreditCardException;
import es.udc.ws.app.client.service.exceptions.ClientTicketsAlreadyPickedException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) {
        // TODO
        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientGameService clientGameService =
                ClientGameServiceFactory.getService();


        if("-a".equalsIgnoreCase(args[0]) ){
            validateArgs(args, 5, new int[] {3, 4});

            //[addGame] GameServiceClient -a <visitantName> <celebrationDate> <priceGame> <ticketMaxCount>
            try {
                ClientGameDto gameDto = clientGameService.addGame(new ClientGameDto(null,
                        args[1], args[2], Double.parseDouble(args[3]),
                        Integer.parseInt(args[4]), Integer.parseInt(args[4])));

                System.out.println("Game with Id: " + gameDto.getGameId() +
                        ", Visitant Name: " + gameDto.getVisitantName() +
                        ", Celebration Date: " +gameDto.getCelebrationDate() +
                        ", Price Game: " + gameDto.getPriceGame() +
                        ", Available Tickets: " + gameDto.getAvailableTickets() +
                        ", Ticket Max Count: " + gameDto.getTicketMaxCount() + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }else if("-d".equalsIgnoreCase(args[0])){
            validateArgs(args, 2, new int[] {});

            // [findByDate] GameServiceClient -d <specifiedDate>
            try {
                List<ClientGameDto> games = clientGameService.findGameByDate(args[1]);
                System.out.println("Found " + games.size() +
                        " games(s) between actual date and date '" + args[1] + "'\n");
                for (int i = 0; i < games.size(); i++) {
                    ClientGameDto gameDto = games.get(i);
                    System.out.println("Id: " + gameDto.getGameId() +
                            ", Visitant Name: " + gameDto.getVisitantName() +
                            ", Celebration Date: " +gameDto.getCelebrationDate() +
                            ", Price Game: " + gameDto.getPriceGame() +
                            ", Available tickets: " + gameDto.getAvailableTickets() +
                            ", Ticket Max Count: " + gameDto.getTicketMaxCount());
                }
            } catch (InputValidationException ex) {
                ex.printStackTrace(System.err);
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
            }


        }else if("-f".equalsIgnoreCase(args[0])){
            validateArgs(args, 2, new int[] {1});

            // [findById] GameServiceClient -f <gameId>
            try {
                ClientGameDto gameDto = clientGameService.findGameById(Long.valueOf(args[1]));

                System.out.println("Found game with Id: " + args[1] +
                        ", Visitant Name: " + gameDto.getVisitantName() +
                        ", Celebration Date: " +gameDto.getCelebrationDate() +
                        ", Price Game: " + gameDto.getPriceGame() +
                        ", Available Tickets: " + gameDto.getAvailableTickets()+
                        ", Ticket Max Count: " + gameDto.getTicketMaxCount());

            } catch (NumberFormatException | InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }else if("-p".equalsIgnoreCase(args[0])){
            validateArgs(args, 5, new int[] {2, 4});

            // [purchase] GameServiceClient -p <userEmail> <gameId> <creditCardNumber> <units>
            try {
                ClientPurchaseDto purchaseDto  = clientGameService.purchaseGame(args[1],
                        Long.valueOf(args[2]), args[3], Integer.parseInt(args[4]));


                System.out.println("Game with Id: " + purchaseDto.getGameId() +
                        " purchased succesfully with purchase Id: " + purchaseDto.getPurchaseId() +
                        ", User Email: " + purchaseDto.getUserEmail() +
                        ", Last four digits of credit card: " + purchaseDto.getLastFourDigits() +
                        ", Bought units: " + purchaseDto.getUnits()+
                        ", Purchase date: " + purchaseDto.getPurchaseDate() +
                        ", Tickets picked: " + purchaseDto.getPickedUp());

            } catch (NumberFormatException | InstanceNotFoundException |
                     InputValidationException | ClientNotEnoughUnitsException | ClientGameAlreadyPlayedException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }else if("-u".equalsIgnoreCase(args[0])){
            validateArgs(args, 2, new int[] {});

            //[findUserPurchases] GameServiceClient -u <userEmail>
            try {
                List<ClientPurchaseDto> purchases = clientGameService.findUserPurchases(args[1]);
                System.out.println("Found " + purchases.size() +
                        " purchases(s) from '" + args[1] + "'\n");
                for (int i = 0; i < purchases.size(); i++) {
                    ClientPurchaseDto purchasesDto = purchases.get(i);
                    System.out.println("Purchase Id: " + purchasesDto.getPurchaseId() +
                            ", Game Id: " + purchasesDto.getGameId() +
                            ", User Email: " + purchasesDto.getUserEmail() +
                            ", Last four digits of credit card: " + purchasesDto.getLastFourDigits() +
                            ", Units bought: " + purchasesDto.getUnits()+
                            ", Purchase date: " + purchasesDto.getPurchaseDate() +
                            ", Tickets picked: " + purchasesDto.getPickedUp());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }else if("-t".equalsIgnoreCase(args[0])){
        validateArgs(args, 3, new int[] {1});

        //[pickTickets] GameServiceClient -t <purchaseId> <creditCardNumber>
        try {
            clientGameService.pickedTickets(Long.valueOf(args[1]),args[2]);
            System.out.println("Tickets from purchase Id: " + args[1] +
                    " with credit card number: '" + args[2] + "'" + " picked");

        }
        catch (NumberFormatException | InstanceNotFoundException |
               InputValidationException | ClientTicketsAlreadyPickedException | ClientNotValidCreditCardException ex) {
            ex.printStackTrace(System.err);
        }
        catch (Exception ex) {
            ex.printStackTrace(System.err);
        }

    }
    }
    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [addGame]              GameServiceClient -a <visitantName> <celebrationDate> <priceGame> <ticketMaxCount>\n" +
                "    [findByDate]           GameServiceClient -d <specifiedDate>\n" +
                "    [findById]             GameServiceClient -f <gameId>\n" +
                "    [purchaseGame]         GameServiceClient -p <userEmail> <gameId> <creditCardNumber> <units>\n" +
                "    [findUserPurchases]    GameServiceClient -u <userEmail>\n" +
                "    [pickTickets]          GameServiceClient -t <purchaseId> <creditCardNumber>\n");
    }
}