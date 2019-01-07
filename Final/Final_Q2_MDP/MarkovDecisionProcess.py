import csv


def read_file(Tfile='transitions1.csv', Rfile='rewards1.csv'):
    """
    Read transition and reward table from file.
    :param Tfile: Transition table file
    :param Rfile: Reward table file
    :return: Transitions, Rewards
    """
    Transitions = {}
    Rewards = {}
    with open(Tfile, 'r') as file:
        reader = csv.reader(file, delimiter=',')
        for row in reader:
            if row[0] in Transitions:
                if row[1] in Transitions[row[0]]:
                    Transitions[row[0]][row[1]].append((float(row[3]), row[2]))
                else:
                    Transitions[row[0]][row[1]] = [(float(row[3]), row[2])]
            else:
                Transitions[row[0]] = {row[1]: [(float(row[3]), row[2])]}

    with open(Rfile, 'r') as file:
        reader = csv.reader(file, delimiter=',')
        for row in reader:
            if row[0] in Rewards:
                if row[1] in Rewards[row[0]]:
                    Rewards[row[0]][row[1]].append(float(row[2]))
                else:
                    Rewards[row[0]][row[1]] = float(row[2])
            else:
                Rewards[row[0]] = {row[1]: float(row[2])}

    return Transitions, Rewards


class MarkovDecisionProcess:

    def __init__(self, transition_matrix, reward_matrix, beta=0.9):
        self.states = transition_matrix.keys()
        self.transition_matrix = transition_matrix
        self.reward_matrix = reward_matrix
        self.beta = beta

    def reward(self, state, action):
        """
        The reward of taking action given state
        :param state:
        :param action:
        :return:
        """
        return self.reward_matrix[state][action]

    def actions(self, state):
        """
        The available actions in given state.
        :param state:
        :return:
        """
        return self.transition_matrix[state].keys()

    def transitions(self, state, action):
        """
        The probability of transitions given current state and action.
        :param state: current state
        :param action: action to take
        :return: a list of (probability, result state) pair
        """
        return self.transition_matrix[state][action]

    def value_iteration(self, epsilon=0.001):
        """
        Solve the process through value iteration.
        :param epsilon: the maximum error allowed
        :return: optimal value function.
        """
        V_prime = {s: 0 for s in self.states}
        while True:
            V = V_prime.copy()
            delta = 0
            for s in self.states:
                V_prime[s] = max([sum([p * self.reward(s, a) + p * self.beta * V[s_prime] for (p, s_prime) in self.transitions(s, a)])
                                  for a in self.actions(s)])
                delta = max(delta, abs(V_prime[s] - V[s]))
            if delta < epsilon * (1 - self.beta) / self.beta:
                return V

    def best_policy(self, V):
        """
        The best policy for every state, which is to take the action with highest value.
        :param V:
        :return:
        """
        pi = {}
        for s in self.states:
            pi[s] = max(self.actions(s), key=lambda a: self.action_value(a, s, V))
        return pi

    def action_value(self, a, s, V):
        """
        The optimal action value function.
        :param a:
        :param s:
        :param V: optimized value function
        :return:
        """
        return self.reward(s, a) + self.beta * sum([p * V[s_prime] for (p, s_prime) in self.transitions(s, a)])


# To test, please comment out the code for one question at a time

# # ------------Q1 & Q2------------
# Transitions, Rewards = read_file()
# mdp = MarkovDecisionProcess(transition_matrix=Transitions, reward_matrix=Rewards)
# V = mdp.value_iteration()
# print('State\tValue')
# for s in V:
#     print(s, '\t', V[s])
# pi = mdp.best_policy(V)
# print('\nOptimal policy:\nState\tAction')
# for s in pi:
#     print(s, '\t', pi[s])

# # -------------Q3--------------
# Transitions, Rewards = read_file('Transitions2.csv', 'Rewards2.csv')
# mdp = MarkovDecisionProcess(transition_matrix=Transitions, reward_matrix=Rewards)
# V = mdp.value_iteration()
# print('State\tValue')
# for s in V:
#     print(s, '\t', V[s])
# pi = mdp.best_policy(V)
# print('\nOptimal policy:\nState\tAction')
# for s in pi:
#     print(s, '\t', pi[s])
# print('action value function:')
# for s in V:
#     print(s)
#     for a in mdp.actions(s):
#         print('\t', a, mdp.action_value(a, s, V))

# # -------------Q4--------------
# Transitions, Rewards = read_file()
# betas = [0.1, 0.3, 0.5, 0.7, 0.9, 0.99]
# for beta in betas:
#     mdp = MarkovDecisionProcess(transition_matrix=Transitions, reward_matrix=Rewards, beta=beta)
#     V = mdp.value_iteration()
#     pi = mdp.best_policy(V)
#     print('\nOptimal policy for beta=', beta, ':\nState\tAction')
#     for s in pi:
#         print(s, '\t', pi[s])

# ------------Bonus-------------



