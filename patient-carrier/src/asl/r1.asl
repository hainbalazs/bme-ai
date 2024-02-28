// mars robot 1



//Initial beliefs 


at(P) :- pos(P,X,Y) & pos(r1,X,Y).

pos(base, 9, 9).


//itt legyen szám, hogy milyen messze kell vinni (v. hova)
!handle(patient, dist).

+!handle(patient) <- !at(base);
					!take(patient, r2);
					!at(base).


//Lépkedő
+!at(L) : at(L).
+!at(L) <- ?pos(L,X,Y);
           move_towards(X,Y);
           !at(L).
		   
+!take(S,L) : true
   <- !ensure_pick(S);
      !at(L);
      drop(S).

+!ensure_pick(S) : garbage(r1)
   <- pick(garb);
      !ensure_pick(S).
+!ensure_pick(_).



dist(base, 10).
dist(dest, 20).

// gets the price for the product,
// (a random value between 100 and 110).
price(Task,X) :- dist(base, B) & dist(dest, D) & X = B + D.

plays(initiator,c).

/* Plans */
// send a message to initiator introducing myself // as a participant
+plays(initiator,In)
        :  .my_name(Me)
        <- .send(In,tell,introduction(participant,Me)).
		
		
		
// answer a Call For Proposal
@c1 +cfp(CNPId,Task)[source(A)]
: plays(initiator,A) & price(Task,Offer)
<- +proposal(CNPId,Task,Offer); // remember my proposal
           .send(A,tell,propose(CNPId,Offer)).
		   
@r1 +accept_proposal(CNPId)
: proposal(CNPId,Task,Offer)
<- .print("My proposal ’",Offer,"’ won CNP ",CNPId,
                  " for ",Task,"!").
				  
@r2 +reject_proposal(CNPId)
        <- .print("I lost CNP ",CNPId, ".");
-proposal(CNPId,_,_). // clear memory

		   
/*
//Initial goal 

//!handle(patient).


!check(slots).

//Plans 


+!check(slots) : not garbage(r1)
   <- next(slot);
      !check(slots).
+!check(slots).


@lg[atomic]
+garbage(r1) : not .desire(carry_to(r2))
   <- !carry_to(r2).

+!carry_to(R)
   <- // remember where to go back
      ?pos(r1,X,Y);
      -+pos(last,X,Y);

      // carry garbage to r2
      !take(garb,R);

      // goes back and continue to check
      !at(last);
      !check(slots).


*/
