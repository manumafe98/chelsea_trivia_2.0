import { Answer } from "../types/answer";
import { Options } from "../types/options";
import { Player } from "../types/player";
import { Question } from "../types/question";
import { AllAttributes, NumericAttributes } from "../utils/attributes";
import { AttributeRelatedQuestioneKeys, AttributeRelatedQuestions, ComparisonRelatedQuestioneKeys, ComparisonRelatedQuestions, MostRelatedQuestioneKeys, MostRelatedQuestions, PlayerRelatedQuestioneKeys, PlayerRelatedQuestions } from "../utils/questions";
import { getRandomEnumValue } from "./getRandomEnumValue";
import { useAxios } from "./useAxios";

export const getQuestion = async (): Promise<Question> => {
    const endpoints = ["/random", "/random", "/random", "/random", "/most"];
    const randomIndex = Math.floor(Math.random() * endpoints.length);
    const randomEndpoint = endpoints[randomIndex];

    if (randomEndpoint === "/most") {
        return await getMostRelatedQuestion();
    };
    return await getRandomRelatedQuestion();
};

const getRandomRelatedQuestion = async (): Promise<Question> => {
    const randomAttribute = getRandomEnumValue(AllAttributes);
    const players: Player[] = await useAxios(`${import.meta.env.BACKEND_URL}/api/v1/player/random?attribute=${randomAttribute}`);
    const randomPlayer = players[Math.floor(Math.random() * players.length)];
    return randomizeQuestionType(randomPlayer, players, randomAttribute);
};

const getMostRelatedQuestion = async (): Promise<Question> => {
    const randomAttribute = getRandomEnumValue(NumericAttributes);
    const question = MostRelatedQuestions[randomAttribute as MostRelatedQuestioneKeys]();
    const players: Player[] = await useAxios(`${import.meta.env.BACKEND_URL}/api/v1/player/most?attribute=${randomAttribute}`);
    const attributes: Options = players.map(player => player.fullName);
    const answer: Answer = players.reduce((max, player) => (player[randomAttribute] > max[randomAttribute] ? player : max), players[0]).fullName;

    return {question, options: attributes, answer, attribute: randomAttribute, type: "MostRelatedQuestions"};
};

const randomizeQuestionType = (player: Player, players: Player[], attribute: AllAttributes): Question => {
    const randomNumber = Math.floor(Math.random() * 3) + 1;
    let question = "";
    let answer: Answer = "";
    let type = "";
    let options: Options = [];

    switch (randomNumber) {
        case 1:
            question = PlayerRelatedQuestions[attribute as PlayerRelatedQuestioneKeys](player.fullName);
            options = players.map(player => Array.isArray(player[attribute]) ? player[attribute].join(", ") : player[attribute]) as Options;
            answer = player[attribute];
            type = "PlayerRelatedQuestions";
            break;
        case 2:
            question = (AttributeRelatedQuestions[attribute as AttributeRelatedQuestioneKeys] as (value: string | number | string[] | number[]) => string)(Array.isArray(player[attribute]) ? player[attribute].join(", ") : player[attribute]);
            answer = player.fullName;
            options = players.map(player => player.fullName);
            type = "AttributeRelatedQuestions";
            break;
        case 3:
            const numericAttribute = getRandomEnumValue(NumericAttributes);
            question = ComparisonRelatedQuestions[numericAttribute as ComparisonRelatedQuestioneKeys]();
            options = players.map(player => player.fullName);
            answer = players.reduce((max, player) => (player[numericAttribute] > max[numericAttribute] ? player : max), players[0]).fullName;
            type = "ComparisonRelatedQuestions";
            break;
    };
    return {question, options, answer, attribute, type};
};
