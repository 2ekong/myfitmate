function GeneralButton({ children, type = 'button', onClick, className = 'btn' }) {
  return (
    <button type={type} onClick={onClick} className={className}>
      {children}
    </button>
  );
}

export default GeneralButton;