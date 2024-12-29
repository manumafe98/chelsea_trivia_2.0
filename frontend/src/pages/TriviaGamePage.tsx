import { PageHeader } from "../components/PageHeader";
import { TriviaContainer } from "../components/TriviaContainer";

export const TriviaGamePage = () => {
  return (
    <div className="page-container">
      <PageHeader/>
      <TriviaContainer/>
    </div>
  );
};
