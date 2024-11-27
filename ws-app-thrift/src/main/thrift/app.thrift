namespace java es.udc.ws.app.thrift

struct ThriftGameDto {
    1: i64 gameId
    2: string visitantName
    3: string celebrationDate
    4: double priceGame
    5: i32 ticketMaxCount
    6: i32 soldTickets
}

struct ThriftPurchaseDto {
    1: i64 gameId
    2: i64 purchaseId
    3: string userEmail
    4: string lastFourDigits
    5: i32 units
    6: string purchaseDate
    7: bool pickedUp
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftGameAlreadyPlayedException {
    1: i64 gameId
    2: string celebrationDate
}

exception ThriftNotEnoughUnitsException {
    1: i64 gameId
    2: i32 units
    3: i32 soldTickets
}

exception ThriftNotValidCreditCardException {
    1: string creditCardNumber
    2: i64 purchaseId
}

exception ThriftTicketsAlreadyPickedException {
    1: i64 purchaseId
}

service ThriftGameService {

   ThriftGameDto addGame(1: ThriftGameDto gameDto) throws (1: ThriftInputValidationException e)

   list<ThriftGameDto> findGameByDate(1: string specifiedDate) throws (1: ThriftInputValidationException e)

   ThriftGameDto findGameById(1: i64 gameId) throws (1: ThriftInstanceNotFoundException e)

   ThriftPurchaseDto purchaseGame(1: string userEmail, 2: i64 gameId, 3: string creditCardNumber, 4: i32 units) throws (1: ThriftInputValidationException e, 2: ThriftInstanceNotFoundException ee, 3: ThriftGameAlreadyPlayedException eee, 4: ThriftNotEnoughUnitsException eeee)

   list<ThriftPurchaseDto> findUserPurchases(1: string userEmail) throws (1: ThriftInputValidationException e)

   void pickedTickets(1: i64 purchaseId, 2: string creditCardNumber) throws (1: ThriftInputValidationException e, 2: ThriftInstanceNotFoundException ee, 3: ThriftNotValidCreditCardException eee, 4: ThriftTicketsAlreadyPickedException eeee)
}