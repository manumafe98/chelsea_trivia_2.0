import { PageHeader } from "../components/PageHeader";
import { TriviaContainer } from "../components/TriviaContainer";

export const TriviaGamePage = () => {
  return (
    <div className="h-screen bg-secondary-blue">
      <PageHeader/>
      <TriviaContainer/>
    </div>
  )
}
