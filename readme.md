# Artificial Intelligence, Intelligent Distributed Systems (BME)

This repository contains coursework for Artificial Intelligence (VIMIAC16) [bayesian-nets, q-learning] and Intelligent Distributed Systems (VIMIAC02) [patient-carrier] at Budapest University of Technology and Economics.

## Bayesian Networks
The goal of this project to implement Bayesian Graphical models in Java and use them for probabilistic inference, by computing marginals from conditionals.

## Q-Learning
The code represents a Flappy Bird agent using Q-learning. It defines classes for storing state data and the Q-table. The agent selects actions based on Q-values, balancing exploration and exploitation. During training, it updates Q-values based on observed rewards and transitions between states. After training, it may perform final actions or set flags for testing.
It is a really illustrative but yet simple demonstration on how to teach an agent to play the well-known Flappy Bird game by Reinforcement Learning, without any deep learning tools. Note: the visual framework for the Flappy Bird game was provided in the handout, and it is the intellectual property of the lecturers.

## Patient Carrier
### Overview
In this project we defined a multi agent environment: nurses in a hospital, who carry patients. Their goal is to help to place the incoming and already diagnosed patient in the most optimal and fastest possible way according to their problem.
Patients that require hospitalisation are prioritised for placement within the institution according to their type of illness and condition. 
The hospital environment consists of a reception area and several wards. The reception area is the entrance, from where new arrivals start their journey through the hospital. The hospital wards are the places where patients are cared for, the aim of patients arriving at the reception is to be assigned to the right ward.
Patients' complaints are categorised according to the departments present in the hospital, and the patient can be treated in the department that corresponds to them. The appropriate department for the patient will always exist. The patient's recovery is automatic, after a certain time in the ward he or she is cured, leaves the ward without further intervention and the vacant place is freed.
The system keeps track of the occupancy of each hospital ward, the location of patients and the distances between wards, hospital reception and available agents. These are used to ensure optimal utilisation of the facility.

### Implementation
The problem is tackled using a modular approach, with distinct responsibilities for agents and their environment. Two types of agents are created: "carrier" agents for patient transportation and "manager" agents for task assignment. Managers detect new arrivals and assign carriers to transport patients based on auction protocols. Carriers move patients between locations and participate in auctions by recalculating bids based on their positions. The Contract Net Protocol (CNP) facilitates task distribution between agents. Managers notify subscribed agents of new tasks, and carriers calculate bids based on their situation. The manager selects the winning bid based on distance, and the winning carrier is assigned the task. Although the CNP allows agents to refuse participation, all carriers bid in each auction. The protocol concludes with the announcement of the winning bid, but additional steps, such as confirmation from the winning agent, are not implemented in the current solution.

For a more detailed documentation refer to: specification.pdf (Translated from Hungarian to English via Bing - grammatical and contextual might arise).

### Technological stack
Java, Jason, AgentSpeak/ASL (https://github.com/jason-lang/jason)

## Authorship
Projects [bayesian-nets, q-learning] were enterily implemented by Balázs Hain (excluding the graphical framework for the Flappy Bird testbench), and [patient-carrier] was implemented in collaboration with Tamás Lukács and Márton Bankó.
