import { Answer } from "./answer"
import { Options } from "./options"
export interface Question {
    question: string,
    options: Options,
    answer: Answer
}
