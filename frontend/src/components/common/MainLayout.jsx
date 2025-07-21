function MainLayout({ children, transparent }) {
  return (
    <main
      className={`main-layout ${transparent ? 'transparent-layout' : ''}`}
      style={{
        maxWidth: '800px',
        margin: '0 auto',
        padding: '24px',
      }}
    >
      {children}
    </main>
  );
}

export default MainLayout;
