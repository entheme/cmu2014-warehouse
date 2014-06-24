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

#define CMU
//#define HOTEL

// Servo Motor Control
#define LTSERVOPIN  5          // Left servo pin
#define RTSERVOPIN  6          // Right servo pin

// PWM values for Servo
//#define L_FWD_H  136   // +45
//#define L_FWD_M  100   // +9
#define L_FWD_L  96    // +5
#define L_STOP   91
#define L_BWD_L  86    // -5
//#define L_BWD_M  82    // -9
//#define L_BWD_H  46    // -45

//#define R_BWD_F  135   // +45
//#define R_BWD_M  99    // +9
#define R_BWD_L  95    // +5
#define R_STOP   90
#define R_FWD_L  85    // -5
//#define R_FWD_M  81    // -9
//#define R_FWD_H  45    // -45

#define ADJ_NONE  0
#define ADJ_RIGHT 1
#define ADJ_LEFT  2
#define ADJ_STOP  3

// Client socket port#
#define PORTID    550             // IP socket port#

// QTI Sensor Control
#define  QTIL    9
#define  QTIC    8
#define  QTIR    2
#define  COLOR_THRESHOLD    30
#define  COLOR_WHITE        0x0
#define  COLOR_BLACK        0x1

#define  SENSOR_NONE        0x0
#define  SENSOR_L           0x1
#define  SENSOR_C           0x2
#define  SENSOR_LC          0x3
#define  SENSOR_R           0x4
#define  SENSOR_LR          0x5
#define  SENSOR_CR          0x6
#define  SENSOR_LCR         0x7

#define  ZONE_INV_IN        1
#define  ZONE_INV_OUT       0

#define API_ERR  -1
#define API_OK   0

#define CMD_ERR             -1
#define CMD_MIN             0
#define CMD_STOP            0
#define CMD_MOVE            1
#define CMD_RECOVERY        2
#define CMD_FWD             3
#define CMD_BWD             4
#define CMD_TURN            5
#define CMD_READ_QTI        6
#define CMD_TEST            7
#define CMD_MAX             7

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

unsigned long Delay_For_90_Degree = 1300;
int curPosition = 0;
int pos = 0;

int curZone = ZONE_INV_IN;
int newZone = ZONE_INV_IN;

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
   
   // send error status ==> 'E' + data
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
         case 'R':
            cmd = CMD_RECOVERY;
            break;
         case 'F':
            cmd = CMD_FWD;
            break;
         case 'B':
            cmd = CMD_BWD;
            break;
         case 'T':
            cmd = CMD_TURN;
            break;
         case 'Q':
            cmd = CMD_READ_QTI;
            break;
         case 'E':
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
             turnRightDegree(90);
             break;
        case CMD_READ_QTI:
             getSensorStatus();
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
  char sVal = 0, lineVal = 0;
  boolean isArrival = false;
  
  Serial.print("moveNextInventory...\n");
   
   while (isArrival == false)
   {
     sVal = getSensorStatus();
     curZone = getZone(sVal, curZone);
     
     if (curZone == ZONE_INV_IN)
       lineVal = (0x7 & (~sVal));
     else
       lineVal = (0x7 & sVal);
     Serial.print("lineVal = 0x");
     Serial.println(lineVal, HEX);
     
     switch (lineVal)
     {
       case SENSOR_NONE: // 0x0
         adjustCenter();
         break;
       case SENSOR_L:    // 0x1
         adjustCenterUsingLeftTurn();
         break;
       case SENSOR_C:    // 0x2
         moveForward();
         break;
       case SENSOR_R:    // 0x4
         adjustCenterUsingRightTurn();
         break;
       case SENSOR_CR:   // 0x6 ---> This is arrival case!
         moveToEnd();
         turnRightDegree(90);
         moveBackward();
         delay(500);
         stopRobot();
         adjustCenter();
         isArrival = true;
         break;
       case SENSOR_LCR:  // 0x7
       case SENSOR_LC:   // 0x3
       case SENSOR_LR:   // 0x5
         Serial.print("Someting wrong...0x");
         Serial.print(sVal, HEX);
         //adjustCenter();
         break;
       default:
         Serial.println("unknown sensor value");
         break;
     }
   }
}

int getZone(char val, int prevZone)
{
  int zone = prevZone;
  
  switch (val)
  {
     case SENSOR_NONE: // 0x0
       zone = ZONE_INV_OUT;
       break;
     case SENSOR_L:    // 0x1
       break;
     case SENSOR_C:    // 0x2
       zone = ZONE_INV_OUT;
       break;
     case SENSOR_LC:   // 0x3
       break;
     case SENSOR_R:    // 0x4
       break;
     case SENSOR_LR:   // 0x5
       zone = ZONE_INV_IN;
       break;
     case SENSOR_CR:   // 0x6
       break;
     case SENSOR_LCR:  // 0x7
       zone = ZONE_INV_IN;
       break;
     default:
       Serial.println("unknown sensor value");
       break;
  }
  Serial.print("Zone: ");
  Serial.print(prevZone);
  Serial.print(" -> ");
  Serial.println(zone);
  
  return zone;
}

boolean adjustCenter()
{
  Serial.println("adjustCenter.............");
  boolean isFound = false;
  int adjDegree[7] = {5, 15, 30, 60, 90, 140, 180}; // ==> {-5 , +10 , -20 , +40 , -50 , +90 , -90}
  int i;
  unsigned long adjDelay;
  unsigned long tS, tE, tD;
  char sVal = 0;
  
  for (i=0 ; i < 7 && isFound == false ; i++)
  {
    adjDelay = getDelayForDegree(adjDegree[i]);
    
    tS = millis();
    
    if (i%2)
    {
      Serial.print("search Right: ");
      turnRight();
    }
    else
    {
      Serial.print("search Left: ");
      turnLeft();
    }
      
    do
    {
      sVal = getOnLineStatus();
      if (sVal == SENSOR_C)
      {
        isFound = true;
        break;
      }
      tE = millis();
      tD = tE - tS;
    } while (tD < adjDelay);
    
    Serial.print(adjDegree[i], DEC);
    Serial.print(" degree, ");
    Serial.print(adjDelay, DEC);
    Serial.print(" msec, tD=");
    Serial.print(tD, DEC);
    Serial.print(", isFound=");
    Serial.println(isFound, DEC);
    
    stopRobot();
  }
  
  return isFound;
}

unsigned long getDelayForDegree(int degree)
{
  unsigned long tDelay;
  tDelay = (Delay_For_90_Degree * degree) / 90;
  return tDelay;
}

void adjustCenterUsingRightTurn()
{
  Serial.println("adjustCenterUsingRightTurn");
  char sVal = 0;
  turnRight();
  while (getOnLineStatus() != SENSOR_C)
    delay(10);
  stopRobot();
}

void adjustCenterUsingLeftTurn()
{
  Serial.println("adjustCenterUsingLeftTurn");
  char sVal = 0;
  turnLeft();
  while (getOnLineStatus() != SENSOR_C)
    delay(10);
  stopRobot();
}

// Turn x degrees to the right 
void turnRightDegree(int degree)
{
  Serial.print("turnRightDegree: ");
  Serial.println(degree);
  unsigned long tDelay;
  if (degree <= 0 || degree > 90)
  {
    Serial.println("invalid degree");
    return;
  }
  Serial.println("tDelay");
  tDelay = (Delay_For_90_Degree * degree) / 90;
  Serial.println(tDelay);
  
  RtServo.write(R_STOP);
  LtServo.write(L_FWD_L);
  delay(tDelay);
  RtServo.write(R_STOP);
  LtServo.write(L_STOP);
}

// Turn x degrees to the left
void turnLeftDegree(int degree)
{
  Serial.print("turnLeftDegree: ");
  Serial.println(degree);
  int tDelay;
  if (degree <= 0 || degree > 90)
  {
    Serial.println("invalid degree");
    return;
  }
  
  tDelay = (Delay_For_90_Degree * degree) / 90;
  
  RtServo.write(R_STOP);
  LtServo.write(L_BWD_L);
  //RtServo.write(R_FWD_L);
  //LtServo.write(R_STOP);
  delay(tDelay);
  RtServo.write(R_STOP);
  LtServo.write(L_STOP);
}

// move to the end on inventory (until sensor = SENSOR_LCR)
void moveToEnd()
{
  Serial.println("moveToEnd");
  char sVal = 0;
  moveForward();
  while (getSensorStatus() != SENSOR_LCR)
  {
    ;
  }
  stopRobot();
}

char getOnLineStatus()
{
  Serial.print("getOnLineStatus: 0x");
  char sVal, lineVal;
  sVal = getSensorStatus();
  
  curZone = getZone(sVal, curZone);
  if (curZone == ZONE_INV_IN)
    lineVal = 0x7 & (~sVal);
  else
    lineVal = 0x7 & sVal;
    
  Serial.println(lineVal, HEX);
  
  return lineVal;
}

char getSensorStatus()
{
   char retVal = SENSOR_NONE;
   char valL, valC, valR;
   
   valL = readSensor(QTIL);
   valC = readSensor(QTIC);
   valR = readSensor(QTIR);
   
   retVal = (valL << 0) | (valC << 1) | (valR << 2);
   Serial.print("Sensor: ");
   Serial.print(valR, DEC);
   Serial.print(valC, DEC);
   Serial.println(valL, DEC);
   Serial.print("retVal: 0x");
   Serial.println(retVal, HEX);

   return retVal;
}

char readSensor(int port)
{
   long val = 0;
   if (port != QTIL && port != QTIC && port != QTIR)
   {
      Serial.print("Error:: invalid QTI port ");
      Serial.print(port);
      return API_ERR; 
   }
   
   val = RCTime(port);
   //Serial.print("Port ");
   //Serial.print(port);
   //Serial.print(", val = ");
   //Serial.println(val);
   if (val < COLOR_THRESHOLD)
      return COLOR_WHITE; // = 0x0
   else
      return COLOR_BLACK; // = 0x1
}

void initRobot()
{
   Serial.print("initRobot...\n");
}
void moveForward()
{
   Serial.println("moveForward");
   RtServo.write(R_FWD_L);
   LtServo.write(L_FWD_L);
}
void moveBackward()
{
   Serial.println("moveBackward");
   RtServo.write(R_BWD_L);
   LtServo.write(L_BWD_L);
}
void turnRight()
{
   Serial.println("turnRight");
   RtServo.write(R_STOP);
   LtServo.write(L_FWD_L);
}
void turnLeft()
{
   Serial.println("turnLeft");
   RtServo.write(R_STOP);
   LtServo.write(L_BWD_L);
   //RtServo.write(R_FWD_L);
   //LtServo.write(L_STOP);
}
void stopRobot()
{
   Serial.println("stopRobot");
   LtServo.write(L_STOP);
   RtServo.write(R_STOP);
}

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


