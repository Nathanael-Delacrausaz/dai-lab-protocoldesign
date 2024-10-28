## 1) overview : 
It's a client-server protocol. The client types a calcul and send it to the server. The server process the calcul and send the answer to the client. 

## 2) transport layer protocol :
The protocol uses TCP. The client establishes the connection. It has to know the server adress IP
The server closes the connection when the answer has been send or when the error message has been sent

## 3) Messages :
OPERATION operand1 operand2 

supported operations : ADD, SUB, MULT, DIV

The operands have to be number. Error response if it's not the case

## 4) 
### example 2 
Client connects to server
Server sends a welcome message
The client sends the operation to the server
THe server process the operation and send the reponse
The server close the tcp connection 

### example 1 
Client connects to server
Server sends a welcome message
The client sends the operation to the server with letters
The server send a error message 
The server close the tcp connection 