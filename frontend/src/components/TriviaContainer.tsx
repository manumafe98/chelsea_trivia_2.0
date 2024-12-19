import { Button } from "./ui/button"

export const TriviaContainer = () => {
  return (
    <div className="flex flex-col justify-center items-center w-1/3 h-5/6 rounded-xl bg-[#bba150] shadow-xl border-2 border-solid border-[#0d4579]">
      <div className="flex items-center justify-center h-2/6 w-2/3 bg-white my-10 rounded-xl border-2 border-solid border-[#7d7045]">
        QUESTION 1
      </div>
      <Button className="bg-[#042457] w-2/3 h-16 border-2 border-solid border-[#7d7045] my-2 text-[#bba150]">1</Button>
      <Button className="bg-[#042457] w-2/3 h-16 border-2 border-solid border-[#7d7045] my-2 text-[#bba150]">2</Button>
      <Button className="bg-[#042457] w-2/3 h-16 border-2 border-solid border-[#7d7045] my-2 text-[#bba150]">3</Button>
    </div>
  )
}
