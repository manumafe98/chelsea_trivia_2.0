type TimerProps = {
  children: React.ReactNode;
};

export const Timer = ({children}: TimerProps) => {
  return (
    <p className="absolute top-0 right-0 p-10 trivia-text">{children}</p>
  );
};
