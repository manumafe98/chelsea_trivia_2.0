import { forwardRef } from "react";
import { Button } from "./Button";

export const InstructionsDialog = forwardRef<HTMLDialogElement>((_, ref) => {

  const closeInstructions = () => {
    if (ref && typeof ref !== "function") {
      ref.current?.close();
    };
  };

  return (
    <dialog
      ref={ref}
      className="dialog-container"
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
        <Button onClick={() => closeInstructions()}>Close</Button>
      </div>
    </dialog>
  );
});
