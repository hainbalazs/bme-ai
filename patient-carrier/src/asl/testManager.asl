/* Rules */
all_proposals_received(CNPId) :-
.count(introduction(participant,_),NP) & // number of participants
.count(propose(CNPId,_), NO) & // number of proposes received
.count(refuse(CNPId), NR) & // number of refusals received
NP = NO + NR.


/* Initial goals */
!startCNP(1,fix(computer_123)).

//!newPatient(1, 1, 12).


/* Plans */

//A new patient arrives
+!newPatient(CNPId, PatientId, LocTo)
<- +auction_state(CNPId, propose);
.findall(Name,introduction(carrier,Name),LP);
.send(LP,tell,bidPatient(CNPId, PatientId, LocTo));
.concat("+!contract(",CNPId,")",Event);
.at("now +4 seconds", Event).


/*
// start the CNP
+!startCNP(Id,Object)
<- .wait(2000); // wait participants introduction
+cnp_state(Id,propose); // remember the state of the CNP
.findall(Name,introduction(participant,Name),LP);
.print("Sending CFP to ",LP);
.send(LP,tell,cfp(Id,Object));
.concat("+!contract(",Id,")",Event);
// the deadline of the CNP is now + 4 seconds, so
// the event +!contract(Id) is generated at that time
.at("now +4 seconds", Event).*/

// receive proposal
// if all proposal have been received, don�t wait for the deadline
@r1 +propose(CNPId,Offer)
: auction_state(CNPId,propose) & all_proposals_received(CNPId)
<- !contract(CNPId).

// receive refusals
@r2 +refuse(CNPId)
: auction_state(CNPId,propose) & all_proposals_received(CNPId)
<- !contract(CNPId).

// this plan needs to be atomic so as not to accept
// proposals or refusals while contracting
@lc1[atomic]
+!contract(CNPId)
: auction_state(CNPId,propose)
<- -+auction_state(CNPId,contract);
.findall(offer(O,A),propose(CNPId,O)[source(A)],L);
.print("Offers are ",L);
L \== []; // constraint the plan execution to at least one offer
.min(L,offer(WOf,WAg)); // sort offers, the first is the best
.print("Winner is ",WAg," with ",WOf);
!announce_result(CNPId,L,WAg);
-+auction_state(Id,finished).

// nothing todo, the current phase is not �propose�
@lc2 +!contract(CNPId).

-!contract(CNPId)
<- .print("CNP ",CNPId," has failed!").

+!announce_result(_,[],_).
// announce to the winner

+!announce_result(CNPId,[offer(O,WAg)|T],WAg)
<- .send(WAg,tell,accept_proposal(CNPId));
!announce_result(CNPId,T,WAg).

// announce to others
+!announce_result(CNPId,[offer(O,LAg)|T],WAg)
<- .send(LAg,tell,reject_proposal(CNPId));
!announce_result(CNPId,T,WAg).

