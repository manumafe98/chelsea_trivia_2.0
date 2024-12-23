import { Answer } from "../types/answert";
import { Options } from "../types/options";
import { Player } from "../types/player";
import { Question } from "../types/question";
import { AllAttributes, NumericAttributes } from "../utils/attributes";
import { MostRelatedQuestions } from "../utils/questions";
import { getRandomEnumValue } from "./getRandomEnumValue";
import { useAxios } from "./useAxios";

export const getQuestion = async (): Promise<Question> => {
    const endpoints = ["/random", "/random", "/random", "/random", "/most"];
    const randomIndex = Math.floor(Math.random() * endpoints.length);
    const randomEndpoint = endpoints[randomIndex];

    if (randomEndpoint === "/most") {
        return await getMostRelatedQuestion();
    }
    return await getRandomRelatedQuestion();
}

const getRandomRelatedQuestion = (): Promise<Question> => {
    const randomAttribute = getRandomEnumValue(AllAttributes);
}

const getMostRelatedQuestion = async (): Promise<Question> => {
    const randomAttribute = getRandomEnumValue(NumericAttributes);
    const question = MostRelatedQuestions[randomAttribute as keyof typeof MostRelatedQuestions]();
    const players: Player[] = await useAxios(`/backend/api/v1/player/most?attribute=${randomAttribute}`);
    const attributes: Options = players.map(player => player[randomAttribute]);
    const answer: Answer = players.reduce((max, player) => (player[randomAttribute] > max[randomAttribute] ? player : max), players[0])[randomAttribute];

    return {question, options: attributes, answer};
}
