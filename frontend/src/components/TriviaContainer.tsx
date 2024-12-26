import { useState } from "react";
import { getQuestion } from "../hooks/getQuestion";
import { Question } from "../types/question";;

export const TriviaContainer = () => {
  const ATTRIBUTE_RELATED_QUESTIONS = "AttributeRelatedQuestions";
  const PLAYER_RELATED_QUESTIONS = "PlayerRelatedQuestions";
  const PROFILE_IMAGE_URL = "profileImageUrl";
  const DUMMY_QUESTION = { attribute: "", answer: "", type: "", question: "", options: ["1", "2", "3"] };
  const[question, setQuestion] = useState<Question>(DUMMY_QUESTION);
  const[startPlaying, setStartPlaying] = useState<boolean>(false);
  const[currentQuestionNumber, setCurrentQuestionNumber] = useState<number>(1);
  const[questionAmountMode, setQuestionAmountMode] = useState<number>(0);
  const[isQuestionAmountModeSet, setIsQuestionAmountModeSet] = useState<boolean>(false);
  const[isTimedMode, setIsTimedMode] = useState<boolean>(false);
  const[isTimedModeSet, setIsTimedModeSet] = useState<boolean>(false);

  const setTimedMode = (isTimedMode: boolean) => {
    setIsTimedMode(isTimedMode);
    setIsTimedModeSet(true);
  };

  const setGameModeAmount = async (numberOfQuestions: number) => {
    setQuestionAmountMode(numberOfQuestions);
    setIsQuestionAmountModeSet(true);
    setQuestion(await getQuestion());
  };

  const getNextQuestion = async () => {
    if (currentQuestionNumber < questionAmountMode) {
      setQuestion(await getQuestion());
      setCurrentQuestionNumber(currentQuestionNumber + 1);
    };
  };

  return (
    <div key={question.question} className="flex flex-col justify-center items-center w-1/3 h-5/6 rounded-xl bg-secondary-gold shadow-xl border-2 border-solid border-primary-blue">
      {!startPlaying && (
        <>
          <button className="trivia-option-button" onClick={() => setStartPlaying(true)}>Start Playing</button>
          <button className="trivia-option-button">Instructions</button>
        </>
      )}
      {startPlaying && !isTimedModeSet && (
        <>
          <button className="trivia-option-button" onClick={() => setTimedMode(true)}>Play Timed Version</button>
          <button className="trivia-option-button" onClick={() => setTimedMode(false)}>Play Normal Mode</button>
        </>
      )}
      {startPlaying && isTimedModeSet && !isQuestionAmountModeSet && (
          <>
            <button className="trivia-option-button" onClick={() => setGameModeAmount(10)}>10 Questions</button>
            <button className="trivia-option-button" onClick={() => setGameModeAmount(30)}>30 Questions</button>
            <button className="trivia-option-button" onClick={() => setGameModeAmount(50)}>50 Questions</button>
          </>
      )}
      {startPlaying && isTimedModeSet && isQuestionAmountModeSet && (
        <>
          <div className="flex items-center text-center justify-center h-2/6 w-2/3 bg-white my-10 rounded-xl border-2 border-solid border-primary-gold">
            {question.attribute === PROFILE_IMAGE_URL && question.type === ATTRIBUTE_RELATED_QUESTIONS ? (
              <div className="flex flex-col justify-center align-center">
                <p className="p-5 text-secondary-blue italic font-bold text-xl">{question.question.split("?")[0] + "?"}</p>
                <div className="flex justify-center">
                  <img className="trivia-player-image" src={question.question.split("?")[1] as string} alt="profile image url"/>
                </div>
              </div>
            ) : (
              <p className="p-5 text-secondary-blue italic font-bold text-xl">{question.question}</p>
            )}
          </div>
          {question.options.map((option, index) => (
            <button key={index} className="trivia-option-button" onClick={() => getNextQuestion()}>
              {question.attribute === PROFILE_IMAGE_URL && question.type === PLAYER_RELATED_QUESTIONS ? (
                <div className="flex justify-center items-center">
                  <img className="trivia-player-image" src={option as string} alt="profile image url"/>
                </div>
              ) : (
                <p>{option}</p>
              )}
            </button>
          ))}
        </>
      )}
    </div>
  )
}
