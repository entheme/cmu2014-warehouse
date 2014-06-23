/*********************************************************************************************
* File: CommandServer
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2014 Anthony J. Lattanze
*
* Versions:
*	0.01 - 2014.06.10
*
* Description:
*
* This program runs on an Arduio processor with a WIFI shield. This program will act as a 
* general server that runs on the Arbot. Commands are send via a client Java program 
* (RemoteControl.java). This pair of applications serve to provide remote control of the Arbot
* over WiFi. Note that this application acts as a server, the Java program (RemoteControl.java) 
* is the client. Note that fixed IP addresses are not possible for this lab, so you must plug
* the Arbot into your PC to first get the IP address for the client, then you can connect to 
* bot and control it. General flow, is that the client connects to the server (tbe Arbot), sends
* a single command and then closes. The reason for this is that the Arduino WIFI shield socket
* will timeout unless you are streaming data to it. The supported commands are forward, backward
* jog right/left (slight turns), and stop. Other commands can easily be added to this and 
* client applications can easily be extend.
*
* Compilation and Execution Instructions: Compiled using Arduino IDE 1.0.4
*
* Parameters: None
*
************************************************************************************************/

#include <SPI.h>
#include <WiFi.h>
#include <Servo.h> 

//#define CMU
#define HOTEL

// Servo Motor Control
#define LTSERVOPIN  5          // Left servo pin
#define RTSERVOPIN  6          // Right servo pin
#define FULLSTOP  90           // PWM value for servo full stop
#define FULLCW   0             // PWM value for servo
#define FULLCCW  180           // PWM value for servo

// PWM values for Servo
#define L_FWD_F  0     // Forward Full speed
#define L_FWD_H  20    // Forward High speed
#define L_FWD_M  45    // Forward Middle speed
#define L_FWD_L  70    // Forward Low speed
#define L_STOP   90
#define L_BWD_L  110
#define L_BWD_M  135
#define L_BWD_H  160
#define L_BWD_F  180
#define R_BWD_F  0
#define R_BWD_H  20
#define R_BWD_M  45
#define R_BWD_L  70
#define R_STOP   90
#define R_FWD_L  110
#define R_FWD_M  135
#define R_FWD_H  160
#define R_FWD_F  180
#define ADJ_NONE  0
#define ADJ_RIGHT 1
#define ADJ_LEFT  2
#define ADJ_STOP  3

// Client socket port#
#define PORTID 504             // IP socket port#

// QTI Sensor Control
#define  QTIL    9
#define  QTIC    8
#define  QTIR    2
#define  WHITE_THRESHOLD    30
#define  BLACK_THRESHOLD    30
#define  COLOR_BLACK        0
#define  COLOR_WHITE        1
#define  COLOR_UNKNOWN      2
#define  SENSOR_L_ONLY      100
#define  SENSOR_C_ONLY      101
#define  SENSOR_R_ONLY      102
#define  SENSOR_ALL         103
#define  SENSOR_UNKNOWN     199

#define API_ERR  -1
#define API_OK   0

#define CMD_ERR      -1
#define CMD_MIN      0
#define CMD_STOP     0
#define CMD_MOVE     1
#define CMD_FWD      2
#define CMD_BWD      3
#define CMD_TURN     4
#define CMD_READ_QTI 5
#define CMD_TEST     6
#define CMD_MAX      6

int   LtServoVal;              // Left servo PWM value
int   RtServoVal;              // Right servo PWM value
Servo LtServo;                 // Left servo object
Servo RtServo;                 // Right servo object

#if defined(CMU)
char ssid[] = "CMU";           // The network SSID (name) 
#else
char ssid[] = "Shadyside Inn";
char pass[] = "hotel5405";     // The network Password
#endif

int status = WL_IDLE_STATUS;   // The status of the network connections
WiFiServer server(PORTID);     // The WIFI status,.. we are using port 504
char inChar;                   // This is a character sent from the client
char whCmd;                    // This is the actual command character
char command;
IPAddress ip;                  // The IP address of the shield
IPAddress subnet;              // The subnet we are connected to
long rssi;                     // The WIFI shield signal strength
byte mac[6];                   // MAC address of the WIFI shield
boolean done;                  // Loop flag
boolean commandRead;           // Loop flag

int curPosition = 0;
int pos = 0;

void setup() 
{
   // Initialize serial port. This is used for debug.
   Serial.begin(9600); 

   // Initialize servos
   LtServo.attach(LTSERVOPIN);
   RtServo.attach(RTSERVOPIN);
   stopRobot();

   // Attempt to connect to Wifi network indicated by SSID.
   startWifiServer();
   
   // Initialize some status for starting point
   initRobot();
   
   Serial.print("setup...done\n");
} // setup

void loop()
{
   whCmd = getCmdFromWarehouseManager();
   
   if (whCmd >= CMD_MIN && whCmd <= CMD_MAX)
   {
      executeCmd(whCmd);
   }
}

int getCmdFromWarehouseManager()
{
   char cmddata;
   int  cmd = CMD_ERR;
   // Waiting for a new Client.
   WiFiClient client = server.available();
   
   if (client)
   {
      Serial.print("Welcome Client !!!\n");
      while (client.available() == 0) 
      {
         if (!client.connected())
         {
            client.stop();
            return CMD_ERR;
         }
         delay(10);
      }
      
      cmddata = client.read();
      Serial.print( "Cmd: ");
      Serial.println(cmddata);
      
      switch (cmddata)
      {
         case 'S':
            cmd = CMD_STOP;
            break;
         case 'M':
            cmd = CMD_MOVE;
            break;
         case 'F':
            cmd = CMD_FWD;
            break;
         case 'B':
            cmd = CMD_BWD;
            break;
         case 'R':
            cmd = CMD_TURN;
            break;
         case 'Q':
            cmd = CMD_READ_QTI;
            break;
         case 'T':
            cmd = CMD_TEST;
            break;
         default:
            Serial.print( "Invalid Command : " );
            Serial.println( cmddata );
            cmd = CMD_ERR;
            break;
      }
      
      client.stop();
   }
   
   return cmd;
}

void executeCmd(int cmd)
{
    switch (cmd)
    {
        case CMD_STOP:
             stopRobot();
             break;
        case CMD_MOVE:
             moveNextInventory();
             break;
        //------------------------------------
        // Test Cases are below...
        //------------------------------------
        case CMD_FWD:
             moveForward();
             break;
        case CMD_BWD:
             moveBackward();
             break;
        case CMD_TURN:
             turnRight();
             break;
        case CMD_READ_QTI:
             checkSensor();
             break;
        case CMD_TEST:
             break;
        default: // If we don't know what the command is, we just say so and exit.
             Serial.print("Invalid Command : ");
             Serial.println(cmd);
    }
}

void moveNextInventory()
{
  int sVal = 0;
  int prevStatus = ADJ_NONE;
   Serial.print("moveNextInventory...\n");
   
   Serial.print("move forward\n");
   moveForward();
   
   while (prevStatus != ADJ_STOP)
   {
      sVal = checkSensor();
      
      switch (sVal)
      {
         case SENSOR_L_ONLY:
            adjLeft(1);
            prevStatus = ADJ_LEFT;
            break;
         case SENSOR_C_ONLY:
            if (prevStatus == ADJ_LEFT)
            {
               adjRight(1);
               prevStatus = ADJ_NONE;
            }
            else if (prevStatus == ADJ_RIGHT)
            {
               adjLeft(1);
               prevStatus = ADJ_NONE;
            }
            break;
         case SENSOR_R_ONLY:
            adjRight(1);
            prevStatus = ADJ_RIGHT;
            break;
         case SENSOR_ALL:
            stopRobot();
            prevStatus = ADJ_STOP;
            break;
         default:
            Serial.print("unknown sensor value..\n");
            break;
      }
      
      delay(20);    
   }
}

int checkSensor()
{
   int retVal, valL, valC, valR;
   
   valL = getSensorVal(QTIL);
   valC = getSensorVal(QTIC);
   valR = getSensorVal(QTIR);
   
   if (valL == COLOR_BLACK && valC == COLOR_WHITE && valR == COLOR_WHITE)
   {
      Serial.print("sensor...L only\n");
      retVal = SENSOR_L_ONLY;
   }
   else if (valL == COLOR_WHITE && valC == COLOR_BLACK && valR == COLOR_WHITE)
   {
      Serial.print("sensor...C only\n");
      retVal = SENSOR_C_ONLY;
   }
   else if (valL == COLOR_WHITE && valC == COLOR_WHITE && valR == COLOR_BLACK)
   {
      Serial.print("sensor...R only\n");
      retVal = SENSOR_R_ONLY;
   }
   else if (valL == COLOR_BLACK && valC == COLOR_BLACK && valR == COLOR_BLACK)
   {
      Serial.print("sensor...ALL\n");
      retVal = SENSOR_ALL;
   }
   else
      retVal = SENSOR_UNKNOWN;
   
   return retVal;
}

int getSensorVal(int port)
{
   long val = 0;
   if (port != QTIL && port != QTIC && port != QTIR)
   {
      Serial.print("Error:: invalid QTI port ");
      Serial.print(port);
      return API_ERR; 
   }
   
   val = RCTime(port);
   Serial.print("Port ");
   Serial.print(port);
   Serial.print(", val = ");
   Serial.println(val);
   if (val < WHITE_THRESHOLD)
      return COLOR_WHITE;
   else if (val > BLACK_THRESHOLD)
      return COLOR_BLACK;
   
   return COLOR_UNKNOWN;
}

void initRobot()
{
   Serial.print("initRobot...\n");
}
void moveForward()
{
   //LtServo.write(FULLCCW);
   //RtServo.write(FULLCW);
   RtServo.write(R_FWD_L);
   LtServo.write(L_FWD_L);
}
void moveBackward()
{
   //LtServo.write(FULLCW);
   //RtServo.write(FULLCCW);
   RtServo.write(R_BWD_H);
   LtServo.write(L_BWD_H);
}
void turnRight()
{
   RtServo.write(FULLSTOP);
   LtServo.write(L_FWD_L);
   delay(1200);
   RtServo.write(FULLSTOP);
   LtServo.write(FULLSTOP);
}
void turnLeft()
{
   RtServo.write(R_FWD_L);
   LtServo.write(FULLSTOP);
   delay(1200);
   RtServo.write(FULLSTOP);
   LtServo.write(FULLSTOP);
}
void stopRobot()
{
   LtServo.write(FULLSTOP);
   RtServo.write(FULLSTOP);
}
void adjRight(int nStep)
{
}
void adjLeft(int nStep)
{
}
void waitLoad()
{
}
void waitComplete()
{
}

 void old_loop() 
 {
    // Listen for a new client.
   WiFiClient client = server.available();
 
   // Wait until we are connected to the client.
   if (client) 
   {
     Serial.print("Client connected..."); 

     // Here is the command parser. Commands are in the format of
     // command character. 
     
     Serial.println( "Waiting for a command..." );
       
     while ( client.available() == 0 ) 
     {
        // There is no input...
        delay( 500 );
     }     
 
     command = client.read();         // Read a character from the client.  
     Serial.print( "Command:: " );
     Serial.println( command );         
    
     // The command interpreter starts here. Bascally, we act on the single character command.

     switch ( command )
     {
        case 'X': // This is an exit command and disconnects from the client
        case 'x': 
             done= true;
             break;
         
        case 'F': // Drives the bot forward
        case 'f': 
             LtServo.write(FULLCCW);  
             RtServo.write(FULLCW); 
             break;

        case 'B': // Drives the bot backward
        case 'b': 
             LtServo.write(FULLCW);  
             RtServo.write(FULLCCW); 
             break;

        case 'S': // Stops the bot
        case 's': 
             LtServo.write(FULLSTOP);  
             RtServo.write(FULLSTOP); 
             break;
         
        case 'R': // Rolls the bot right then stops after 1/2 second
        case 'r': 
             LtServo.write(FULLCCW);  
             RtServo.write(FULLSTOP);
             delay(500);
             LtServo.write(FULLSTOP);  
             RtServo.write(FULLSTOP);             
             break;

        case 'L': // Rolls the bot left then stops after 1/2 second
        case 'l': 
             LtServo.write(FULLSTOP);  
             RtServo.write(FULLCW);
             delay(500);
             LtServo.write(FULLSTOP);  
             RtServo.write(FULLSTOP);             
             break;
          
        default: // If we don't know what the command is, we just say so and exit.
             Serial.print( "Unrecognized Command:: " );
             Serial.println( command );
    
     } // switch

     // Note that we disconnect here because the Arduino server only stays connected as long
     // as the client streams data. As soon as the client stream stops, the server automatically
     // disconnects from the server... so only one command is sent through at a time

     client.stop();
     Serial.println( "Client Disconnected.\n" );
     Serial.println( "........................." );
     
   } // if client is connected
 
 } // loop

/************************************************************************************************
* Start Wifi Server
************************************************************************************************/
void startWifiServer()
{
   // Attempt to connect to Wifi network indicated by SSID.
   while ( status != WL_CONNECTED) 
   {
     #if 0
     Serial.print("Attempting to connect to SSID: ");
     #else
     Serial.print("####### SSID: ");
     #endif
     Serial.println(ssid);
     #if defined(CMU)
     status = WiFi.begin(ssid);
     #else
     status = WiFi.begin(ssid, pass);
     #endif
   }  
   
   // Print connection information to the debug terminal
   printConnectionStatus();
   
   // Start the server and print a message to the terminial.
   server.begin();
   Serial.println("The Command Server is started.");
   Serial.print("--------------------------------------\n\n");
}
 
/************************************************************************************************
* The following method prints out the connection information
************************************************************************************************/
 void printConnectionStatus() 
 {
     // Print the basic connection and network information: Network, IP, and Subnet mask
     ip = WiFi.localIP();
     Serial.print("Connected to ");
     Serial.print(ssid);
     Serial.print(" IP Address:: ");
     Serial.println(ip);
     subnet = WiFi.subnetMask();
     Serial.print("Netmask: ");
     Serial.println(subnet);
   
     // Print our MAC address.
     WiFi.macAddress(mac);
     Serial.print("WiFi Shield MAC address: ");
     Serial.print(mac[5],HEX);
     Serial.print(":");
     Serial.print(mac[4],HEX);
     Serial.print(":");
     Serial.print(mac[3],HEX);
     Serial.print(":");
     Serial.print(mac[2],HEX);
     Serial.print(":");
     Serial.print(mac[1],HEX);
     Serial.print(":");
     Serial.println(mac[0],HEX);
   
     // Print the wireless signal strength:
     rssi = WiFi.RSSI();
     Serial.print("Signal strength (RSSI): ");
     Serial.print(rssi);
     Serial.println(" dBm");

 } // printConnectionStatus
 
/****************************************************************
* RCTime (int pin) - Determines how long it takes the capactior 
* to charge.
* Parameters:
*              int pin - the pin on the Arduino where the QTI 
*                        sensor is connected.
* Description:
* Check out the QTI schematics and specifications at: 
* http://www.parallax.com/product/555-27401
* This method initalizes the sensor pin as output and charges the
* capacitor on the QTI. The QTI emits IR light which is reflected
* off of any surface in front of the sensor. The amount of IR 
* light reflected back is detected by the IR resistor on the QTI.
* This is the resistor that the capacitor discharges through. The
* amount of time it takes to discharge determines how much light, 
* and therefore the lightness or darkness of the material in
* front of the QTI sensor.
****************************************************************/

long RCTime(int sensorIn)
{
    long duration = 0;
    pinMode(sensorIn, OUTPUT);         // Sets pin as OUTPUT
    digitalWrite(sensorIn, HIGH);      // Pin HIGH
    pinMode(sensorIn, INPUT);          // Sets pin as INPUT
    digitalWrite(sensorIn, LOW);       // Pin LOW
    while(digitalRead(sensorIn) != 0)  // Count until the pin goes
       duration++;                     // LOW (cap discharges)
       
    return duration;                   // Returns the duration of the pulse
}


