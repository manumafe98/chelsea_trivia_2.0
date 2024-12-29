import { useEffect, useRef, useState } from "react";
import { getQuestion } from "../hooks/getQuestion";
import { Answer } from "../types/answer";
import { Question } from "../types/question";
import { Button } from "./Button";
import { InstructionsDialog } from "./InstructionsDialog";
import { Timer } from "./Timer";

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
  const[showScore, setShowScore] = useState<boolean>(false);
  const[score, setScore] = useState<number>(0);
  const[timeLeft, setTimeLeft] = useState<number>(60);
  const[isRunning, setIsRunning] = useState<boolean>(false);
  const openInstructionsDialog = useRef<HTMLDialogElement>(null);

  useEffect(() => {
    let intervalId: number | undefined;

    if (isTimedMode && isRunning && timeLeft > 0) {
      intervalId = window.setInterval(() => {
        setTimeLeft(prevTime => prevTime - 1);
      }, 1000);
    } else if (timeLeft === 0) {
      getNextQuestion(null);
    };

    return () => {
      if (intervalId) {
        window.clearInterval(intervalId);
      }
    };
  }, [isRunning, timeLeft]);

  const setTimedMode = (isTimedMode: boolean) => {
    setIsTimedMode(isTimedMode);
    setIsTimedModeSet(true);
  };

  const setGameModeAmount = async (numberOfQuestions: number) => {
    setQuestionAmountMode(numberOfQuestions);
    setIsQuestionAmountModeSet(true);
    setQuestion(await getQuestion());
  };

  const validateQuestion = (choosenOption: Answer) => {
    const answer = Array.isArray(question.answer) ? question.answer.join(", ") : question.answer;

    if (choosenOption === answer) {
      setScore((prevScore) => prevScore + 1);
    };
  };

  const setQuestionAmount = (amount: number) => {
    setGameModeAmount(amount);
    if (isTimedMode) {
      setIsRunning(true);
    };
  };

  const openInstructions = () => {
    openInstructionsDialog.current?.showModal();
  };

  const getNextQuestion = async (choosenOption: Answer | null) => {
    if (currentQuestionNumber < questionAmountMode) {
      if (choosenOption) {
        validateQuestion(choosenOption);
      };
      setQuestion(await getQuestion());
      setCurrentQuestionNumber((prevQuestionNumber) => prevQuestionNumber + 1);
      setTimeLeft(60);
    } else {
      setIsQuestionAmountModeSet(false);
      setIsTimedModeSet(false);
      setStartPlaying(false);
      setShowScore(true);
      setCurrentQuestionNumber(1);
      setIsRunning(false);
    };
  };

  const playAgain = () => {
    setStartPlaying(true);
    setShowScore(false);
    setScore(0);
  };

  const mainMenu = () => {
    setStartPlaying(false);
    setShowScore(false);
    setScore(0);
  };

  return (
    <section className="trivia-section">
      <InstructionsDialog ref={openInstructionsDialog}/>
      <div className="trivia-container">
        {isTimedMode && isQuestionAmountModeSet && (
          <Timer>{timeLeft}</Timer>
        )}
        {!startPlaying && !showScore && (
          <>
            <Button onClick={() => setStartPlaying(true)}>Start Playing</Button>
            <Button onClick={() => openInstructions()}>Instructions</Button>
          </>
        )}
        {startPlaying && !isTimedModeSet && (
          <>
            <Button onClick={() => setTimedMode(true)}>Play Timed Version</Button>
            <Button onClick={() => setTimedMode(false)}>Play Normal Mode</Button>
          </>
        )}
        {startPlaying && isTimedModeSet && !isQuestionAmountModeSet && (
            <>
              <Button onClick={() => setQuestionAmount(10)}>10 Questions</Button>
              <Button onClick={() => setQuestionAmount(20)}>20 Questions</Button>
              <Button onClick={() => setQuestionAmount(30)}>30 Questions</Button>
            </>
        )}
        {startPlaying && isTimedModeSet && isQuestionAmountModeSet && (
          <>
            <div className="question-container">
              {question.attribute === PROFILE_IMAGE_URL && question.type === ATTRIBUTE_RELATED_QUESTIONS ? (
                <div className="flex flex-col justify-center align-center">
                  <p className="p-5 trivia-text">{question.question.split("?")[0] + "?"}</p>
                  <div className="flex justify-center">
                    <img className="trivia-player-image" src={question.question.split("?")[1] as string} alt="profile image url"/>
                  </div>
                </div>
              ) : (
                <p className="p-5 trivia-text">{question.question}</p>
              )}
            </div>
            {question.options.map((option, index) => (
              <Button key={index} onClick={() => getNextQuestion(option)}>
                {question.attribute === PROFILE_IMAGE_URL && question.type === PLAYER_RELATED_QUESTIONS ? (
                  <div className="flex justify-center items-center">
                    <img className="trivia-player-image" src={option as string} alt="profile image url"/>
                  </div>
                ) : (
                  <p>{option}</p>
                )}
              </Button>
            ))}
          </>
        )}
        {showScore && (
          <>
            <p className="trivia-text">Your score was {score}/{questionAmountMode}</p>
            <Button onClick={() => playAgain()}>Play Again</Button>
            <Button onClick={() => mainMenu()}>Main Menu</Button>
          </>
        )}
      </div>
    </section>
  );
};
