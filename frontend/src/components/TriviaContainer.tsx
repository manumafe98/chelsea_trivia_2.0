
export const TriviaContainer = () => {
  return (
    <div className="flex flex-col justify-center items-center w-1/3 h-5/6 rounded-xl bg-secondary-gold shadow-xl border-2 border-solid border-primary-blue">
      <div className="flex items-center justify-center h-2/6 w-2/3 bg-white my-10 rounded-xl border-2 border-solid border-primary-gold">
        <p className="text-secondary-blue italic font-bold text-xl">QUESTION</p>
      </div>
      <button className="trivia-option-button">1</button>
      <button className="trivia-option-button">2</button>
      <button className="trivia-option-button">3</button>
    </div>
  )
}
