package br.com.aditum.samplejavase;

import java.util.Scanner;

import br.com.aditum.pinpadSdk.api.AditumPaymentApi;
import br.com.aditum.pinpadSdk.model.v1.BaseResponse;
import br.com.aditum.pinpadSdk.model.v1.CancelationResponse;
import br.com.aditum.pinpadSdk.model.v1.DataPickerMenu;
import br.com.aditum.pinpadSdk.model.v1.GetInputRequest;
import br.com.aditum.pinpadSdk.model.v1.GetInputResponse;
import br.com.aditum.pinpadSdk.model.v1.InitRequest;
import br.com.aditum.pinpadSdk.model.v1.InitResponse;
import br.com.aditum.pinpadSdk.model.v1.InstallmentType;
import br.com.aditum.pinpadSdk.model.v1.PaymentRequest;
import br.com.aditum.pinpadSdk.model.v1.PaymentResponse;
import br.com.aditum.pinpadSdk.model.v1.PaymentType;
import br.com.aditum.pinpadSdk.model.v1.PinpadMessages;
import br.com.aditum.pinpadSdk.model.v1.WaitEventRequest;
import br.com.aditum.pinpadSdk.model.v1.WaitEventResponse;
import br.com.aditum.pinpadSdk.model.v1.StatusResponse;

public class Main {

    public static void main(String[] args) {
        AditumPaymentApi api = new AditumPaymentApi("YOUR_PARTNER_TOKEN");
        
        int option = 0;

        do
        {
            System.out.println("Aditum Pinpad API Client Sample Application");
            System.out.println("Enter: ");
            System.out.println("1 - pinpad initialization");
            System.out.println("2 - payment");
            System.out.println("3 - get payment status");
            System.out.println("4 - abort payment process");
            System.out.println("5 - cancel charge");
            System.out.println("6 - show message on pinpad display");
            System.out.println("7 - get input from pinpad");
            System.out.println("8 - wait for pinpad event");
            System.out.println("9 - get service status");

            Scanner in = new Scanner(System.in);
            option = in.nextInt();

            switch (option)
            {
                case 1:
                    InitRequest initRequest = new InitRequest();
                    initRequest.setPinpadMessages(new PinpadMessages(
                            null,
                            null,
                            null,
                            " Sample Java SE ",
                            null
                    ));
                    initRequest.setApplicationName("Java Sample");
                    initRequest.setApplicationVersion("1.0.0");
                    
                    System.out.print("Enter merchant id (cnpj): ");
                    String merchantId = in.next();
                    initRequest.setMerchantId(merchantId);

                    InitResponse initResponse = api.init(initRequest);
                    if(initResponse != null) {
                        System.out.println(initResponse.toString());
                    } else {
                        System.out.println("Failed to initialize!");
                    }
                    break;

                case 2:
                    PaymentRequest payRequest = new PaymentRequest();
                    payRequest.setAmount((long)100);
                    payRequest.setInstallmentNumber(1);
                    payRequest.setInstallmentType(InstallmentType.MERCHANT);
                    payRequest.setPaymentType(PaymentType.ASK_USER);

                    PaymentResponse payResponse = api.pay(payRequest);
                    
                    System.out.println(payResponse.toString());
                    
                    break;

                case 3:

                    PaymentResponse paymentStatus = api.getPaymentStatus();

                    System.out.println(paymentStatus.toString());

                    System.out.println("Payment status: " + paymentStatus.getPaymentStatus().toString());

                    break;

                case 4:
                    BaseResponse abortResponse = api.abort();

                    System.out.println(abortResponse.toString());

                    break;

                case 5:
                    System.out.print("Enter charge ID: ");
                    String chargeId = in.next();

                    CancelationResponse cancelResponse = api.cancel(chargeId);

                    System.out.println(cancelResponse.toString());

                    break;

                case 6:
                    System.out.print("Enter message: ");
                    String message = in.next();

                    BaseResponse displayResponse = api.display(message);

                    System.out.println(displayResponse.toString());

                    break;

                case 7:
                    GetInputRequest getInputRequest = new GetInputRequest();

                    System.out.print("Enter number of menu options: ");
                    Integer numberOfOptions = in.nextInt();

                    DataPickerMenu dataPickerMenu = new DataPickerMenu();

                    for (int i = 0; i < numberOfOptions; i++) {
                        System.out.print("Enter option #" + (i + 1) + ": ");
                        String menuOption = in.next();

                        dataPickerMenu.addOption(menuOption);
                    }

                    System.out.print("Enter menu title: ");
                    dataPickerMenu.setTitle(in.next());

                    getInputRequest.setMenu(dataPickerMenu);

                    GetInputResponse getInputResponse = api.getInput(getInputRequest);

                    System.out.println(getInputResponse.toString());
                    System.out.println("Option selected: " + dataPickerMenu.getOptions().get(getInputResponse.getSelected()));

                    break;

                case 8:
                    WaitEventRequest waitEventRequest = new WaitEventRequest();

                    System.out.print("Wait for key? (y/n) ");
                    waitEventRequest.setWaitKey(in.next().equals("y"));

                    System.out.print("Wait for ICC? (y/n) ");
                    waitEventRequest.setWaitIcc(in.next().equals("y"));

                    System.out.print("Wait for magnetic stripe? (y/n) ");
                    waitEventRequest.setWaitMagneticStripe(in.next().equals("y"));

                    System.out.print("Wait for contactless? (y/n) ");
                    waitEventRequest.setWaitContactless(in.next().equals("y"));

                    WaitEventResponse waitEventResponse = api.waitEvent(waitEventRequest);

                    System.out.println(waitEventResponse.toString());
                    System.out.println("Event detected: " + waitEventResponse.getEventCode());

                    break;

                case 9:
                    StatusResponse statusResponse = api.getStatus();
                    if(statusResponse != null) {
                        System.out.println(statusResponse.getStatus().toString());
                    } else {
                        System.out.println("Service not listening");
                    }
            }
 
        }
        while (option != -1);
    }

}
