type ButtonProps = {
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  key?: React.Key;
  children: React.ReactNode;
};

export const Button = ({ onClick, key, children }: ButtonProps) => {
  return (
    <button className="trivia-button" onClick={onClick} key={key}>
      {children}
    </button>
  )
}
