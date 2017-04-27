# CA-ARBAC
This Android project contains the full code for CA-ARBAC. You can download and run it on Android Emulator or Real device. 
To test CA-ARBAC using some Android application, you need to customize the application such that it makes a call to ASM Simulator instead of existing Android service for resource access. To do so, you have to include the ASM Simulator libraray to the application and 
call it from the application.

The ASM Simulator is a library application that can make a call to CA-ARBAC on behalf of the requesting application.The ASM Simulator first contacts the CA-ARBAC. If access is denied by CA-ARBAC, the ASM Simulator returns security exception message to the application. If access is allowed by CA-ARBAC, the ASM Simulator then contacts Android system and fetches the reference to the requested resource from Android system
   
Two test applications that already contain ASM Simulator libraray are also available for you in the following links 
You can use these two test applications if you don't want to develope your own application.
https://github.com/juharahmed/MyMessengertest 
https://github.com/juharahmed/LocationGetter
 
Note: You should restart your device the first time you install CA-ARBAC as CA-ARBAC contains a background service that is started 
during boot time
