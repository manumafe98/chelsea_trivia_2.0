export type PlayerRelatedQuestioneKeys = keyof typeof PlayerRelatedQuestions;

export const PlayerRelatedQuestions = {
    shirtNumbers: (playerName: string) => `Which are the shirt numbers that ${playerName} used?`,
    profileImageUrl: (playerName: string) => `Who of the following players is ${playerName}?`,
    positions: (playerName: string) => `Which are the positions that ${playerName} played in?`,
    activeAgesAtClub: (playerName: string) => `Which were the active years at the club for ${playerName}?`,
    nationalities: (playerName: string) => `Which are the nationalities of ${playerName}?`,
    appareances: (playerName: string) => `How many appareances does ${playerName} has?`,
    goals: (playerName: string) => `How many goals does ${playerName} has?`,
    assists: (playerName: string) => `How many assists does ${playerName} has?`,
    yellowCardsReceived: (playerName: string) =>  `How many yellow cards does ${playerName} has?`,
    redCardsReceived: (playerName: string) => `How many red cards does ${playerName} has?`,
    minutesPlayed: (playerName: string) => `How many minutes played does ${playerName} has?`
};

export type AttributeRelatedQuestioneKeys = keyof typeof AttributeRelatedQuestions;

export const AttributeRelatedQuestions = {
    shirtNumbers: (shirtNumbers: number[]) => `Who among these players used the following ${shirtNumbers}?`,
    profileImageUrl: (profileImageUrl: string) => `Who is this player?${profileImageUrl}`,
    positions: (positions: number[]) => `Who among these players played as ${positions}?`,
    activeAgesAtClub: (activeAgesAtClub: number[]) => `Who among these players was active for chelsea with the following years ${activeAgesAtClub}?`,
    nationalities: (nationalities: string[]) => `Who among these players has the following nationalities ${nationalities}?`,
    appareances: (appareances: number) => `Who among these players has ${appareances} appareances?`,
    goals: (goals: number) => `Who among these players has ${goals} goals?`,
    assists: (assists: number) => `Who among these players has ${assists} assists?`,
    yellowCardsReceived: (yellowCardsReceived: number) => `Who among these players has ${yellowCardsReceived} yellow cards received?`,
    redCardsReceived: (redCardsReceived: number) => `Who among these players has ${redCardsReceived} red cards received?`,
    minutesPlayed: (minutesPlayed: number) => `Who among these players has ${minutesPlayed} minutes played?`
};

export type ComparisonRelatedQuestioneKeys = keyof typeof ComparisonRelatedQuestions;

export const ComparisonRelatedQuestions = {
    appareances: () => "Who among these players has the most appareances?",
    goals: () => "Who among these players has the most goals?",
    assists: () => "Who among these players has the most assists?",
    yellowCardsReceived: () => "Who among these players has the most yellow cards received?",
    redCardsReceived: () => "Who among these players has the most red cards received?",
    minutesPlayed: () => "Who among these players has the most minutes played?"
}

export type MostRelatedQuestioneKeys = keyof typeof MostRelatedQuestions;

export const MostRelatedQuestions = {
    appareances: () => "Who among these players has the most appareances in chelsea history?",
    goals: () => "Who among these players is chelsea history topscorer?",
    assists: () => "Who among these players is chelsea history top assister?",
    yellowCardsReceived: () => "Who among these players has the most yellow cards received in chelsea history?",
    redCardsReceived: () => "Who among these players has the most red cards received in chelsea history?",
    minutesPlayed: () => "Who among these players has the most minutes played in chelsea history?"
};
