import { useState, useEffect, useRef } from "react";
import { getQuestion } from "../hooks/getQuestion";
import { Question } from "../types/question";
import { Answer } from "../types/answer";

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
    }

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
    if (choosenOption === question.answer) {
      setScore(score + 1);
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

  const closeInstructions = () => {
    openInstructionsDialog.current?.close();
  };

  const getNextQuestion = async (choosenOption: Answer | null) => {
    if (currentQuestionNumber < questionAmountMode) {
      if (choosenOption) {
        validateQuestion(choosenOption);
      };
      setQuestion(await getQuestion());
      setCurrentQuestionNumber(currentQuestionNumber + 1);
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

  return (
    <section className="h-4/5 w-full flex justify-center items-center">
      <dialog
        ref={openInstructionsDialog}
        className="z-10 backdrop:bg-black/45 m-auto rounded-xl min-h-[45vh] max-w-[40vw] shadow-lg border-4 border-solid bg-secondary-blue border-primary-gold"
      >
      <div className="flex flex-col justify-center items-center p-5">
        <span className="text-secondary-gold italic text-md flex flex-col gap-2 mb-2">
          <div className="text-center text-lg font-bold">
            Welcome to the ultimate Chelsea FC trivia experience! Ready to test your knowledge about the Blues?
          </div>
          <div className="flex flex-col gap-4">
            <div className="flex flex-col gap-2">
              <div className="font-semibold">Choose your game mode:</div>
              <ul className="ml-4 flex flex-col gap-1">
                <li>- Relaxed Mode: Take your time with each question</li>
                <li>- Time Attack: Think fast! You'll have 60 seconds per question</li>
              </ul>
            </div>
            <div className="flex flex-col gap-2">
              <div className="font-semibold">Pick how many questions you want:</div>
              <ul className="ml-4 flex flex-col gap-1">
                <li>- Quick Game: 10 questions</li>
                <li>- Standard Game: 20 questions</li>
                <li>- Challenge Mode: 30 questions</li>
              </ul>
            </div>
            <div className="flex flex-col gap-2">
              <div>
                Most questions will test your knowledge about Chelsea players - their stats, names, and how they compare to each other. Think you know your Chelsea stars inside and out? Let's find out!
              </div>
            </div>
          </div>
        </span>
        <button className="trivia-option-button" onClick={() => closeInstructions()}>Close</button>
      </div>
    </dialog>
    <div className="relative flex flex-col justify-center items-center w-1/3 h-5/6 rounded-xl bg-secondary-gold shadow-xl border-4 border-solid border-primary-blue">
      {isTimedMode && isQuestionAmountModeSet && (
        <p className="absolute top-0 right-0 p-10 text-secondary-blue italic font-bold text-xl">{timeLeft}</p>
      )}
      {!startPlaying && !showScore && (
        <>
          <button className="trivia-option-button" onClick={() => setStartPlaying(true)}>Start Playing</button>
          <button className="trivia-option-button" onClick={() => openInstructions()}>Instructions</button>
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
            <button className="trivia-option-button" onClick={() => setQuestionAmount(10)}>10 Questions</button>
            <button className="trivia-option-button" onClick={() => setQuestionAmount(20)}>20 Questions</button>
            <button className="trivia-option-button" onClick={() => setQuestionAmount(30)}>30 Questions</button>
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
            <button key={index} className="trivia-option-button" onClick={() => getNextQuestion(option)}>
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
      {showScore && (
        <>
          <p className="text-secondary-blue italic font-bold text-xl">Your score was {score}</p>
          <button className="trivia-option-button" onClick={() => playAgain()}>Play Again</button>
        </>
      )}
    </div>
    </section>
  )
}
