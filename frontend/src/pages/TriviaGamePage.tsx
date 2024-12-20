import { PageHeader } from "../components/PageHeader"
import { TriviaContainer } from "../components/TriviaContainer"

export const TriviaGamePage = () => {
  return (
    <div className="h-screen">
      <PageHeader/>
      <div className="h-4/5 w-full flex justify-center items-center">
        <TriviaContainer/>
      </div>
    </div>
  )
}
