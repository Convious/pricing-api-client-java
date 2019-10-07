# Convious Pricing API Java client
A Java client library that is meant to simplify communication with the Convious pricing API.
## Instalation
### Maven
```
<dependency>
    <groupId>com.convious</groupId>
    <artifactId>pricing-api-client</artifactId>
    <version>1.0</version>
</dependency>
```
### Gradle
```
implementation 'com.convious:pricing-api-client:1.0.0'
```
### SBT
```
libraryDependencies += "com.convious" % "pricing-api-client" % "1.0.0"
```
## Usage
Create API client using your `clientId` and `clientSecret`:
```
var client = PricingApiClient.create(clientId, clientSecret);
```
Then you can use the client to either post events that communicate changes of your inventory or to retrieve the prices for any of the products. The client has 2 flavours of methods: synchronous and asynchronous (methods end with `Async` suffix and instead of result return `CompletableFuture`)
### Posting inventory events
You can either post a single event using `postEvent(Async)` or post them in batches using `postEvents(Async)` (the latter is recommended)`:
```
client.postEvents(
    new InventoryEvent[] {
        new ProductCreated(
            "productReference",
            "Product name",
            3,
            new ProductPricing(
                new BigDecimal("10.00"),
                new BigDecimal("20.00"),
                new BigDecimal("15.00"),
                new BigDecimal("22.00"),
            )
        ),
        new ProductPricingChanged(
            "productReference",
            new ProductPricing(
                new BigDecimal("10.50"),
                new BigDecimal("20.50"),
                new BigDecimal("15.50"),
                new BigDecimal("22.50"),
            )
        ),
        new ProductAvailabilityChanged(
            "productReference",
            LocalDate.of(2019, 10, 10),
            "10:00:00",
            3
        ),
        new ProductRemoved(
            "productReference"
        ),
    }
);
```
### Getting prices
You can get the prices of your inventory using `getPrices(Async)`:
```
var request = new PricingRequest(
    "cookie id",
    "127.0.0.1",
    LocalDate.of(2019, 10, 1),
    LocalDate.of(2019, 10, 30),
    "Europe/Amsterdam",
    new PricingRequestProduct[] {
        new PricingRequestProduct("productReference", 2),
        new PricingRequestProduct("productReference2", 1),
    },
    new String[] { "10:00:00", "10:30:00", "12:00:00" }
);
var prices = client.getPrices(request);
```
# License
This library is licensed under MIT. Full license text is available in [LICENSE][license].

[license]: https://github.com/Convious/pricing-api-client-java/tree/master/LICENSE