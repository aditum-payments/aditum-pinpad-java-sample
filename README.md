# Aditum Pinpad SDK for Android and Java SE

Use this SDK to interact with pinpads and authorize finantial transactions through Aditum's payment system.

Features:

* [Getting started](#getting-started)
* [Initialize pinpad communication](#initialize-pinpad-communication)
* [Payments](#payments)
* [Pinpad features](#pinpad-features)
* [**Java SE Sample**](samplejavase/src/main/java/br/com/aditum/samplejavase)

For more details, check [API reference](https://docs.aditum.com.br/pinpad) or get in touch with our [tech team](mailto:ti@aditum.com.br).

## Getting started

### Gradle setup

Add the code below to your `build.gradle` file:

```
dependencies {
    ...
    implementation "br.com.aditum:pinpadSdk:2.1.0"
    ...
}

allprojects {
    repositories {
        jcenter()
        maven {
            url "http://artifacts.aditum.com.br:8081/artifactory/gradle-release/Java/"
        }
    }
}
```

### Setup API client

```
AditumPaymentApi api = new AditumPaymentApi("YOUR_PARTNER_TOKEN");
```

Where `YOUR_PARTNER_TOKEN` should be replaced with your partner's token defined by Aditum. If your company don't have one, get in touch with our [commercial team](mailto:ti@aditum.com.br).

## Initialize pinpad communication

```
InitRequest initRequest = new InitRequest();

// Set pinpad messages. If this property (or any message within) is left empty, default
// values are used. 
initRequest.setPinpadMessages(new PinpadMessages(
        "Payment approved",
        "Payment denied",
        "Initializing...",
        " Sample Java SE ",
        "Processing..."
));

// Accept contactless cards:
initRequest.setContactless(true);

// Define serial port name. If left empty, it'll search for all available serial COM ports.
initRequest.setPinpadPortName("ttyACM1");

initRequest.setApplicationName("Java Sample");
initRequest.setApplicationVersion("1.0.0");

// Setup merchant id for transaction. CNPJ or CPF (as registered on Aditum's platform)
initRequest.setMerchantId("MERCHANT_ID"); 

// Send init request to the API and connect to the specified pinpad.
BaseResponse initResponse = api.init(initRequest);
```

## Payments

### Pay

```
PaymentRequest payRequest = new PaymentRequest();

// Set transaction amount:
payRequest.setAmount((long)100);

// Set installment number. Ignore if debit transaction. Default value is 1.
payRequest.setInstallmentNumber(1);

// Set installment type. Supported values are:
// - InstallmentType.MERCHANT
// - InstallmentType.ISSUER
payRequest.setInstallmentType(InstallmentType.MERCHANT);

// Set payment type. Supported values are:
// - ASK_USER (customer enter payment type from pinpad keyboard)
// - DEBIT
// - CREDIT
payRequest.setPaymentType(PaymentType.CREDIT);

// Send payment request to the API:
PaymentResponse payResponse = api.pay(payRequest);
```

This endpoint works asynchronously. Polling must be used to obtain payment status.

### Abort payment process

To abort a [payment request](#pay), use:

```
BaseResponse abortResponse = api.abort();
```

This endpoint aborts a payment process, if allowed. If transaction were already sent to the authorizer, refer to [cancelation section](#cancelation) to cancel an authorized payment.

### Cancelation

```
CancelationResponse cancelResponse = api.cancel("PAYMENT_ID");
```

## Pinpad features

### Display

This endpoint allows showing messages in pinpad's display, that contains 2 rows of 16 characters each.

```
BaseResponse displayResponse = api.display(message);
```

### Data Picker (get input)

This endpoint synchronously allows getting an input through pinpad's keyboard, using up and down keys.

```
GetInputRequest getInputRequest = new GetInputRequest();
DataPickerMenu dataPickerMenu = new DataPickerMenu();

// Setting up menu title:
dataPickerMenu.setTitle("Pick an option");

// Setting up menu options:
dataPickerMenu.addOption("OPTION 1");
dataPickerMenu.addOption("OPTION 2");
dataPickerMenu.addOption("OPTION 3");

getInputRequest.setMenu(dataPickerMenu);

// Send request to the API:
GetInputResponse getInputResponse = api.getInput(getInputRequest);
```

### Pinpad events

The endpoint synchronously waits for a pinpad event.

```
WaitEventRequest waitEventRequest = new WaitEventRequest();

// Setup event filters:
waitEventRequest.setWaitKey(true);
waitEventRequest.setWaitIcc(false);
waitEventRequest.setWaitMagneticStripe(true);
waitEventRequest.setWaitContactless(false);

// Send request to the API:
WaitEventResponse waitEventResponse = api.waitEvent(waitEventRequest);
```