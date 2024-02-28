/* Rules */
all_proposals_received(CNPId) :-
.count(introduction(participant,_),NP) & // number of participants
.count(propose(CNPId,_), NO) & // number of proposes received
.count(refuse(CNPId), NR) & // number of refusals received
NP = NO + NR.

/* Plans */

//A new patient arrives
+newPatient(CNPId, PatientId, LocTo)
<- .wait(10);
+auction_state(CNPId, propose);
.findall(Name,introduction(carrier,Name),LP);
.send(LP,tell,bidPatient(CNPId, PatientId, LocTo));
.concat("+!contract(",CNPId,")",Event);
.at("now +100 milliseconds", Event).

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

