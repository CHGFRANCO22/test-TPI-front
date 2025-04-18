const validUsers = [
  { username: "usuario1", password: "1234" },
  { username: "admin", password: "admin123" }
];

const loginForm = document.getElementById("login-form");
const loginError = document.getElementById("login-error");

loginForm.addEventListener("submit", (e) => {
  e.preventDefault();
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  const user = validUsers.find(
    (u) => u.username === username && u.password === password
  );

  if (user) {
    alert("Inicio de sesión exitoso");
    // Aquí puedes redirigir a la página de turnos
  } else {
    loginError.textContent = "Usuario o contraseña incorrectos.";
  }
});
