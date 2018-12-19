abrirConexion:- odbc_connect('Prolog', _, [ user(root), password('12345'),alias('prolog'),open(once) ]).
cerrarConexion:-odbc_disconnect('prolog').

temperaturaDetieneDesarrollo(Temperatura):-odbc_query('prolog','select(Temperatura) from temperatura where Temperatura>=10 and Temperatura <=12', row(Temperatura)).


temperaturaDesarrolloNormal(Temperatura):-odbc_query('prolog','select(Temperatura) from temperatura where Temperatura>=18 and Temperatura <=25', row(Temperatura)).


temperaturaMayorDesarrollo(Temperatura):-odbc_query('prolog','select(Temperatura) from temperatura where Temperatura>=21 and Temperatura <=30', row(Temperatura)).


humedadOptima(Humedad):-odbc_query('prolog','select(Humedad) from temperatura where Humedad>=60 and Humedad <=80', row(Humedad)).



%%Horas del dia en que se puede abrir el techo  en 6 minutos
horasSol(11).
horasSol(12).
horasSol(13).
horasSol(14).
horasSol(15).
horasSol(16).

menor(X,Y):-X<Y.
mayor(X,Y):-X>Y.
esHoraDeLuzSolar(X):-horasSol(X).


%Humedad esta por debajo del rango permitido
humedadBaja(H):-not(humedadOptima(H)),menor(H,60).
%Humedad sobre pasa el rango permitido
humedadAlta(H):-not(humedadOptima(H)),mayor(H,80).

%se abrira el techo si  hay luz solar y la humedad es optima.
abrirTecho(Hr,H):-esHoraDeLuzSolar(Hr),humedadOptima(H).
%Se encienden los focos cuando la temperatura esta por debajo del rango y el techo no esta abierto
encenderFocos(T,Hr,H):-temperaturaDetieneDesarrollo(T),not(abrirTecho(Hr,H)).
%Regar
temperaturaOptimaParaRegar(T):-temperaturaDesarrolloNormal(T);temperaturaMayorDesarrollo(T).
%regar si la huemdad es baja y temperatura esta en rango.
regar(H,T):-humedadBaja(H),temperaturaOptimaParaRegar(T).
%Se encendera el ventilador cuando la temperatura sobre pase los 30C y se abrira mediotecho
encenderVentilador(T):-T>30.
abrirMedioTecho(T):-T>30.

% Apagar todo.
optimizar(T,H):-temperaturaMayorDesarrollo(T),humedadOptima(H).



%parafloreser luz directa en 6 horas seran minutos






