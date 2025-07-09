function InputField({ label, name, type = 'text', value, onChange, placeholder }) {
  return (
    <label>
      {label}
      <input
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        className="form-input"
      />
    </label>
  );
}

export default InputField;
