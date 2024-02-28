queue(0).

+!updateBid(L) <-
?queue(B);
if(B == 0) {
-bid(_);
?distance(L, O);
+bid(O/2);
}.

at(P) :- pos(P,X,Y) & .my_name(Me) & pos(Me,X,Y).

+!handle(PatientId, LocTo) : handling <- !handle(PatientId, LocTo).

+!handle(PatientId, LocTo) <-
+handling;
.print("Handling patient ", PatientId, " destination: ", LocTo);
!at("Reception");
!at(LocTo);
-handling.


//Lépkedő
+!at(L) : at(L) <-
.print("Arrived at ", L);
arrived;
if(L == "Reception") {pickup;}
else {drop;
?queue(A);
-queue(_);
+queue(A-1);
!updateBid(L);
}.

+!at(L) <- ?pos(L,X,Y);
           move_towards(X,Y);
		   ?bid(Z)[source(self)];
		   -bid(_)[source(self)];
		   +bid(Z-1)[source(self)];
		   .my_name(Me);
		   .print(Me, " bid: ", Z-1);
           !at(L).
		   
		  
price(Task,X) :- bid(X).

plays(initiator,manager).

/* Plans */
// send a message to initiator introducing myself // as a participant
+plays(initiator,In)
        :  .my_name(Me)
        <- .send(In,tell,introduction(carrier,Me)).
		
		
		
// answer a Call For Proposal
@c1 +bidPatient(CNPId,PatientId, LocTo)[source(A)]
: plays(initiator,A) & price(CNPId,Offer)
<- .print("bidnel a LocTo: ",LocTo);
+proposal(CNPId,PatientId,LocTo,Offer); // remember my proposal
           .send(A,tell,propose(CNPId,Offer)).
		   
@r1 +accept_proposal(CNPId)
: proposal(CNPId,PatientId,LocTo,Offer)
<- .print("My proposal ",Offer," won CNP ",CNPId,"!");
?bid(F);
?distance(LocTo, D);
.print("PatientId:",PatientId," LocTo:",LocTo, " Bid before: ", F, " Distance: ", D);
-bid(_)[source(percept)];
-bid(_)[source(self)];
+bid(F+(D));
?queue(A);
-queue(_);
+queue(A+1);
?bid(K);
.print("Bid after: ", K);
!handle(PatientId, LocTo).				  
				  
@r2 +reject_proposal(CNPId)
        <- .print("I lost CNP ",CNPId, ".");
-proposal(CNPId,_,_). // clear memory

