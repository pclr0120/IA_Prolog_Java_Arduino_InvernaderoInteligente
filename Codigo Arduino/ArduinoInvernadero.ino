// Incluimos librería
#include <DHT.h>
#include <Servo.h>

// Declaramos la variable para controlar el servo
Servo  motor1, motor2;
// Definimos el pin digital donde se conecta el sensor
#define DHTPIN 2
// Dependiendo del tipo de sensor
#define DHTTYPE DHT11

// Inicializamos el sensor DHT11
DHT dht(DHTPIN, DHTTYPE);

void setup() {
  // Inicializamos comunicación serie
  Serial.begin(9600);
  motor1.attach(9);
  motor2.attach(8);
  pinMode(13, OUTPUT);
  pinMode(12, OUTPUT);
    pinMode(7, OUTPUT);

  // Comenzamos el sensor DHT
  dht.begin();

}

void loop() {
  // Esperamos 5 segundos entre medidas


  // Leemos la humedad relativa
  int h = dht.readHumidity();
  // Leemos la temperatura en grados centígrados (por defecto)
  int t = dht.readTemperature();
  // Leemos la temperatura en grados Fahreheit
  //float f = dht.readTemperature(true);

  // Comprobamos si ha habido algún error en la lectura
  if (isnan(h) || isnan(t) ) {
    Serial.println("Error obteniendo los datos del sensor DHT11");
    return;
  }

  // Calcular el índice de calor en Fahreheit
  // float hif = dht.computeHeatIndex(f, h);
  // Calcular el índice de calor en grados centígrados
  int hic = dht.computeHeatIndex(t, h, false);

  //Serial.print("Humedad: ");
  Serial.println(h);
  Serial.println(hic);
  delay(2000);

  //Serial.print(" %\t");
  //Serial.print("Temperatura: ");
  //Serial.print(t);
  //Serial.print(" *C ");
  //Serial.print(f);
  //Serial.print(" *F\t");
  //Serial.print("Índice de calor: ");
  //Serial.println(hic);
  //Serial.print(" *C ");
  // Serial.print(hif);
  //Serial.println(" *F");


  int control = Serial.read();

  if (control == '1') {

    AbrirTechoCompleto();
  }
  if (control == '2') {

    AbrirTechoMitad();
  }
  if (control == '0') {
    CerrarTecho();
  }
  if (control == '3') {
    digitalWrite(13, HIGH);

  }
  if (control == '4') {
    digitalWrite(13, LOW);

  }
    if (control == '5') {
    digitalWrite(12, HIGH);

  }
    if (control == '6') {
    digitalWrite(12, LOW);

  }
   
    if (control == '7') {
    digitalWrite(7, HIGH);

  }
    if (control == '8') {
    digitalWrite(7, LOW);

  }


  //AbrirTecho();
}

void AbrirTechoCompleto() {
  motor1.write(180);
  motor2.write(180);

}

void AbrirTechoMitad() {

  motor1.write(90);
  motor2.write(90);

}

void CerrarTecho() {
  motor1.write(0);
  motor2.write(0);
}


