from time import time

import numpy as np
np.random.seed(0)

class FlappyAgent:

    def __init__(self, observation_space_size, action_space, n_iterations):
        self.q_table = np.zeros([*observation_space_size, len(action_space)])
        self.env_action_space = action_space
        self.n_iterations = n_iterations

        self.test = False

    def step(self, state):
        action = 0

        if not self.test:  # and ...:
            # action =
            pass
        else:
            # action =
            pass

        return action

    def epoch_end(self, epoch_reward_sum):
        pass

    def learn(self, old_state, action, new_state, reward):
        pass

    def train_end(self):
        # ...

        self.q_table = None  # TODO
        self.test = True
