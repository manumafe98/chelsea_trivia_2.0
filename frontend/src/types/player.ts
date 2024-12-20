export interface Player {
    id: string;
    fullName: string;
    shirtNumbers: number[];
    profileImageUrl: string;
    positions: string[];
    activeAgesAtClub: number[];
    nationalities: string[];
    appareances: number;
    goals: number;
    assists: number;
    yellowCardsReceived: number;
    redCardsReceived: number;
    minutesPlayed: number;
}
