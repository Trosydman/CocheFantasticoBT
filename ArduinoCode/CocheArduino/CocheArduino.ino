int motorAvanzar = 9;
int motorRetroceder = 10;
int motorDerecha = 6;
int motorIzquierda = 5;
int centralR = 4;
int centralG = 2;
int centralB = 3;
int antiNieblaR = 11;
int antiNieblaG = 7;
int antiNieblaB = 8;
int traseraR = 13;
int traseraGB = 12;

//const int turnLeftPinOUT  = 5;   //DOWN   //naranja
//const int turnRightPinOUT = 6;  //UP      //NEGRO 
//const int forwardPinOUT = 9;
//const int backwardPinOUT = 10;

const int marginalDirection = 30;

char command;            // variable to receive data from the serial port
const char CMD_RIGHT = '5';
const char CMD_LEFT  = '6';
const char CMD_CENTER  = '7';
const char CMD_FORWARD = '3';
const char CMD_BACKWARD = '4';
const char CMD_LEDON = '1';
const char CMD_LEDOFF = '2';
const int POS_MAX_RIGHT = 1024;
const int POS_CENTER = POS_MAX_RIGHT / 2;
const int POS_MIN_LEFT = 0;

const int MAX_SPEED = 255;   //Speed goes from -255 to 255
const int SPEED_STEP = 220;
int currentSpeed =  0;
int safetyTime = 7000;

long timeMark,timeMark2;
boolean tramaCompleta = false;
char c;
String strRecibido = "";

float fltVelocidad = 0.0;
float fltGiro = 0.0;
String strColor= "";

String delantera;
String central;
String trasera;

////////////

void setup() {  
  pinMode(motorAvanzar, OUTPUT);
  pinMode(motorRetroceder, OUTPUT);
  pinMode(motorDerecha, OUTPUT);
  pinMode(motorIzquierda, OUTPUT);
  pinMode(centralR, OUTPUT);
  pinMode(centralG, OUTPUT);
  pinMode(centralB, OUTPUT);
  pinMode(antiNieblaR, OUTPUT);
  pinMode(antiNieblaG, OUTPUT);
  pinMode(antiNieblaB, OUTPUT);
  pinMode(traseraR, OUTPUT);
  pinMode(traseraGB, OUTPUT);
  //lucesApagar();
  lucesIniciales();

  Serial.begin(9600);      
  timeMark = millis();
}

void loop() {

          //Parpadear cada byte recibido
        /* if (Serial.available()){
            //Lectura de caracteres   
            command = Serial.read();
            if (command !=',' && command != ';'){
                //Suma de caracteres en variable string
                strRecibido += command;  
            }else if (command ==','){
                //Guarda variable 1, velocidad
                fltVelocidad = strRecibido.toFloat();
                //Borra la variable string para almacenar nuevos datos
                strRecibido=""; 
            }else if (command ==';'){
                //Guarda variable 2, giro
                fltGiro = strRecibido.toFloat();
                //Borra la variable string para almacenar nuevos datos
                strRecibido=""; 
                tramaCompleta = true;
           }*/
           
           if (Serial.available()){    
              c = Serial.read();
              //if(c == '*') {
                //c = Serial.read();
                while ( c != '#') {           //Hasta que el caracter sea intro
                       Serial.print(c);
                       strRecibido = strRecibido + c ;
                        c = Serial.read();
                  }
                  Serial.println(""+strRecibido+"");
                  //Serial.println();
                  delay(25);
              //}
//              int checksum = strRecibido.substring(0, strRecibido.indexOf("/")).toInt();
              strRecibido = strRecibido.substring(strRecibido.indexOf("*"), strRecibido.indexOf("C")+2);
//              strRecibido = strRecibido.substring(strRecibido.indexOf("*"), checksum-1);
              Serial.print(strRecibido.indexOf("*"));
              Serial.print("-");
              Serial.print(strRecibido.indexOf("C")+2);
              Serial.println(" ----> "+strRecibido+" <----");
//              tramaCompleta = true;
              fltVelocidad = strRecibido.substring(strRecibido.indexOf("*")+1,strRecibido.indexOf(",")).toFloat();// Aqui muestro cada par de dato separado en su textbox
              Serial.println(strRecibido.substring(strRecibido.indexOf("*")+1,strRecibido.indexOf(","))+" => "+fltVelocidad);
              fltGiro = strRecibido.substring(strRecibido.indexOf(",")+1,strRecibido.indexOf(";")).toFloat();
              Serial.println(strRecibido.substring(strRecibido.indexOf(",")+1,strRecibido.indexOf(";"))+" => "+ fltGiro);
              strColor = strRecibido.substring(strRecibido.indexOf(";")+1,strRecibido.length());
              Serial.println("//////////////");
              Serial.println(fltVelocidad);
              Serial.println("//////////////");
              Serial.println(fltGiro);
              Serial.println("//////////////");
              Serial.println(strColor);
              Serial.println("//////////////");
              Serial.println("//////////////");
              delantera = strColor.substring(strColor.indexOf("A"),strColor.indexOf("B"));
              central = strColor.substring(strColor.indexOf("B"),strColor.indexOf("C"));
              trasera = strColor.substring(strColor.indexOf("C"),strColor.indexOf("C")+2);
              Serial.println("D: "+delantera+"\nI: "+central+"\nT: "+trasera);
           }

//           if (tramaCompleta){
//                tramaCompleta = false;
                if(fltGiro < 0 ){
                    //fltGiro = fltGiro * 511 / 255;
                    fltGiro = -fltGiro;
                    analogWrite(motorIzquierda, 255);//fltGiro
                    analogWrite(motorDerecha, 0); //255-fltGiro
                    Serial.print("Giro izquierda: ");
                    Serial.println(fltGiro);
                    Serial.println(command);
                }else if(fltGiro > 0){
                    //fltGiro = fltGiro * 511 / 255;
                    analogWrite(motorDerecha, 255); //fltGiro
                    analogWrite(motorIzquierda, 0);
                    Serial.print("Giro Derecha: ");
                    Serial.println(fltGiro);
                    Serial.println(command);
                }else{
                    analogWrite(motorDerecha, 0);
                    analogWrite(motorIzquierda, 0);
                }
        
                if(fltVelocidad >= 1){
                    analogWrite(motorAvanzar, fltVelocidad);
                    analogWrite(motorRetroceder, 255-fltVelocidad);
                    Serial.print("Velocidad avanzar: ");
                    Serial.println(fltVelocidad);
                    Serial.println(command);
                    tramaCompleta = false;
                }else if (fltVelocidad < 0){
                    fltVelocidad = -fltVelocidad;
                    analogWrite(motorRetroceder, fltVelocidad);
                    analogWrite(motorAvanzar, 255-fltVelocidad);
                    Serial.print("Velocidad retroceder: ");
                    Serial.println(fltVelocidad);
                    Serial.println(command);
                    tramaCompleta = false;
                }else{
                  analogWrite(motorRetroceder, fltVelocidad);
                  analogWrite(motorAvanzar, fltVelocidad);
                  tramaCompleta = false;
                }

//                String delantera = strColor.substring(strColor.indexOf("A"),strColor.indexOf("B"));
//                String central = strColor.substring(strColor.indexOf("B"),strColor.indexOf("C"));
//                String trasera = strColor.substring(strColor.indexOf("C"),strColor.indexOf("C")+2);
//                Serial.println("D: "+delantera+"\nI:"+central+"\nT:"+trasera);
                if(delantera == "A0"){
                    //Apagar luces delanteras
                     apagarLucesDelanteras();
                }else if(delantera == "A1"){
                    //Luz Roja Delantera
                     luzRojaDelantera();
                }else if(delantera == "A2"){
                    //Luz Verde Delantera
                     luzVerdeDelantera();
                }else if(delantera == "A3"){
                    //Luz Naranja Delantera
                     luzNaranjaDelantera();
                }else if(delantera == "A4"){
                    //Luz Azul Delantera
                     luzAzulDelantera();
                }else if(delantera == "A5"){
                    //Luz Morada Delantera
                     luzMoradaDelantera();
                }else if(delantera == "A6"){
                    //Luz Blanca Delantera
                     luzBlacaDelantera();
                }else if(delantera == "A7"){
                    //Luces de fiesta
                     lucesMulticolor();
                }else if(delantera == "A8"){
                     //Luces emergencia
                     lucesEmergencia();
                }else{
                     //Luz Azul Delantera
                     luzAzulDelantera();
                }

                if(central == "B0"){
                    //Apagar luces centrales
                    apagarLucesCentrales();
                }else if(central == "A1"){
                    //Luz Roja Central
                    luzRojaCentral();
                }else if(central == "B2"){
                    //Luz Verde Central
                    luzVerdeCentral();
                }else if(central == "B3"){
                    //Luz Naranja Central
                    luzNaranjaCentral();
                }else if(central == "B4"){
                    //Luz Azul Central
                    luzAzulCentral();
                }else if(central == "B5"){
                    //Luz Morada Central
                    luzMoradaCentral();
                }else if(central == "B6"){
                   //Luz Blanca Central
                   luzBlacaCentral();
                }else{
                     //Apagar luces centrales
                    apagarLucesCentrales();
                }
            
                if(trasera == "C0"){
                    //Apagar luces traseras
                    apagarLucesTrasera();
                }else if(trasera == "C1"){
                    //Luz Roja Trasera
                    luzRojaTrasera();
                }else if(trasera == "C2"){
                    //Luz Verde/Azul Trasera
                    luzVerdeAzulTrasera();
                }else if(trasera == "C3"){
                    //Luz Blanca Trasera
                    luzBlancaTrasera();
                }else{
                    //Luz Roja Trasera
                    luzRojaTrasera();  
                }
            
                 
//            }
/*        } else { //Si no llega ningun comando vamos a la posicion central y paramos el motor
            delay(25);
            if (!Serial.available()){
                analogWrite(motorAvanzar, 0);
                analogWrite(motorRetroceder, 0);
                analogWrite(motorDerecha, 0);
                analogWrite(motorIzquierda, 0);
                Serial.println("no recibe");
            }
        }
    delay(25);
*/ 
}



void luzRojaTrasera(){
  digitalWrite(traseraR, HIGH);
  digitalWrite(traseraGB, LOW);
}

void luzBlancaTrasera(){
  digitalWrite(traseraR, HIGH);
  digitalWrite(traseraGB, HIGH);
}

void luzVerdeAzulTrasera(){
  digitalWrite(traseraR, LOW);
  digitalWrite(traseraGB, HIGH);
}

void apagarLucesTrasera(){
  digitalWrite(traseraR, LOW);
  digitalWrite(traseraGB, LOW);
}

void luzBlacaCentral(){
  digitalWrite(centralR, LOW);
  digitalWrite(centralG, LOW);
  digitalWrite(centralB, LOW);
}

void luzMoradaCentral(){
  digitalWrite(centralR, LOW);
  digitalWrite(centralG, HIGH);
  digitalWrite(centralB, LOW);
}

void luzAzulCentral(){
  digitalWrite(centralR, HIGH);
  digitalWrite(centralG, HIGH);
  digitalWrite(centralB, LOW);
}

void luzVerdeCentral(){
  digitalWrite(centralR, HIGH);
  digitalWrite(centralG, LOW);
  digitalWrite(centralB, HIGH);
}

void luzNaranjaCentral(){
  digitalWrite(centralR, LOW);
  digitalWrite(centralG, LOW);
  digitalWrite(centralB, HIGH);
}

void luzRojaCentral(){
  digitalWrite(centralR, LOW);
  digitalWrite(centralG, HIGH);
  digitalWrite(centralB, HIGH);
}

void apagarLucesCentrales(){
  digitalWrite(centralR, HIGH);
  digitalWrite(centralG, HIGH);
  digitalWrite(centralB, HIGH);
}

void apagarLucesDelanteras(){
  digitalWrite(antiNieblaR, HIGH);
  digitalWrite(antiNieblaG, HIGH);
  digitalWrite(antiNieblaB, HIGH);
}

void luzRojaDelantera(){
  digitalWrite(antiNieblaR, LOW);
  digitalWrite(antiNieblaG, HIGH);
  digitalWrite(antiNieblaB, HIGH);
}

void luzVerdeDelantera(){
  digitalWrite(antiNieblaR, HIGH);
  digitalWrite(antiNieblaG, LOW);
  digitalWrite(antiNieblaB, HIGH);
}

void luzNaranjaDelantera(){
  digitalWrite(antiNieblaR, LOW);
  digitalWrite(antiNieblaG, LOW);
  digitalWrite(antiNieblaB, HIGH);
}

void luzAzulDelantera(){
  digitalWrite(antiNieblaR, HIGH);
  digitalWrite(antiNieblaG, HIGH);
  digitalWrite(antiNieblaB, LOW);
}

void luzMoradaDelantera(){
  digitalWrite(antiNieblaR, LOW);
  digitalWrite(antiNieblaG, HIGH);
  digitalWrite(antiNieblaB, LOW);
}

void luzBlacaDelantera(){
  digitalWrite(antiNieblaR, LOW);
  digitalWrite(antiNieblaG, LOW);
  digitalWrite(antiNieblaB, LOW);
}

void lucesMulticolor(){
  //for (int x=0; x<3; x++){
      digitalWrite(centralR, LOW);
      digitalWrite(centralG, HIGH);
      digitalWrite(centralB, HIGH);
      digitalWrite(antiNieblaR, LOW);
      digitalWrite(antiNieblaG, HIGH);
      digitalWrite(antiNieblaB, HIGH);
      digitalWrite(traseraR, HIGH);
      digitalWrite(traseraGB, LOW);
      delay(2500);
    
      digitalWrite(centralR, HIGH);
      digitalWrite(centralG, LOW);
      digitalWrite(centralB, HIGH);
      digitalWrite(antiNieblaR, HIGH);
      digitalWrite(antiNieblaG, LOW);
      digitalWrite(antiNieblaB, HIGH);
      digitalWrite(traseraR, LOW);
      digitalWrite(traseraGB, HIGH);
      delay(2500);
    
      digitalWrite(centralR, LOW);
      digitalWrite(centralG, LOW);
      digitalWrite(centralB, HIGH);
      digitalWrite(antiNieblaR, LOW);
      digitalWrite(antiNieblaG, LOW);
      digitalWrite(antiNieblaB, HIGH);
      digitalWrite(traseraR, HIGH);
      digitalWrite(traseraGB, HIGH);
      delay(2500);
    
      digitalWrite(centralR, HIGH);
      digitalWrite(centralG, HIGH);
      digitalWrite(centralB, LOW);
      digitalWrite(antiNieblaR, HIGH);
      digitalWrite(antiNieblaG, HIGH);
      digitalWrite(antiNieblaB, LOW);
      digitalWrite(traseraR, LOW);
      digitalWrite(traseraGB, LOW);
      delay(2500);
      
      digitalWrite(centralR, LOW);
      digitalWrite(centralG, HIGH);
      digitalWrite(centralB, LOW);
      digitalWrite(antiNieblaR, LOW);
      digitalWrite(antiNieblaG, HIGH);
      digitalWrite(antiNieblaB, LOW);
      digitalWrite(traseraR, HIGH);
      digitalWrite(traseraGB, LOW);
      delay(2500);
      
      digitalWrite(centralR, LOW);
      digitalWrite(centralG, LOW);
      digitalWrite(centralB, LOW);
      digitalWrite(antiNieblaR, LOW);
      digitalWrite(antiNieblaG, LOW);
      digitalWrite(antiNieblaB, LOW);
      digitalWrite(traseraR, LOW);
      digitalWrite(traseraGB, HIGH);
      delay(2500);
  //}
}

void lucesEmergencia(){
  //for (int x=0; x<3; x++){
      digitalWrite(antiNieblaR, LOW);
      digitalWrite(antiNieblaG, LOW);
      digitalWrite(antiNieblaB, HIGH);
    
      digitalWrite(centralR, LOW);
      digitalWrite(centralG, LOW);
      digitalWrite(centralB, HIGH);
    
      digitalWrite(traseraR, HIGH);
      digitalWrite(traseraGB, LOW);
    
      delay(300);
    
      digitalWrite(antiNieblaR, HIGH);
      digitalWrite(antiNieblaG, HIGH);
      digitalWrite(antiNieblaB, HIGH);
    
      digitalWrite(centralR, HIGH);
      digitalWrite(centralG, HIGH);
      digitalWrite(centralB, HIGH);
    
      digitalWrite(traseraR, LOW);
      digitalWrite(traseraGB, LOW);
    
      delay(300);
  //}  
}

void lucesApagar(){
  digitalWrite(centralR, HIGH);
  digitalWrite(centralG, HIGH);
  digitalWrite(centralB, HIGH);
  digitalWrite(antiNieblaR, HIGH);
  digitalWrite(antiNieblaG, HIGH);
  digitalWrite(antiNieblaB, HIGH);
  digitalWrite(traseraR, LOW);
  digitalWrite(traseraGB, LOW);
}

void lucesIniciales(){
  digitalWrite(centralR, HIGH);
  digitalWrite(centralG, HIGH);
  digitalWrite(centralB, HIGH);
  digitalWrite(antiNieblaR, HIGH);
  digitalWrite(antiNieblaG, HIGH);
  digitalWrite(antiNieblaB, LOW);
  digitalWrite(traseraR, HIGH);
  digitalWrite(traseraGB, LOW);
}

