// Simular usuarios registrados
let usuariosRegistrados = [];

// Manejar registro de usuarios
const registerForm = document.getElementById("register-form");
const registerSuccess = document.getElementById("register-success");
const registerError = document.getElementById("register-error");

registerForm.addEventListener("submit", (e) => {
  e.preventDefault();
  
  const nombre = document.getElementById("register-name").value;
  const email = document.getElementById("register-email").value;
  const password = document.getElementById("register-password").value;

  const usuarioExistente = usuariosRegistrados.find(u => u.email === email);

  if (usuarioExistente) {
    registerError.textContent = "El correo ya está registrado.";
  } else {
    usuariosRegistrados.push({ nombre, email, password });
    registerSuccess.style.display = "block";
    registerError.textContent = "";
    registerForm.reset();
  }
});

// Cambiar entre registro e inicio de sesión
document.getElementById("go-to-login").addEventListener("click", () => {
  document.getElementById("register-container").style.display = "none";
  document.getElementById("login-container").style.display = "block";
});

document.getElementById("go-to-register").addEventListener("click", () => {
  document.getElementById("login-container").style.display = "none";
  document.getElementById("register-container").style.display = "block";
});

// Manejar inicio de sesión
const loginForm = document.getElementById("login-form");
const loginError = document.getElementById("login-error");

loginForm.addEventListener("submit", (e) => {
  e.preventDefault();
  
  const email = document.getElementById("login-email").value;
  const password = document.getElementById("login-password").value;

  const usuario = usuariosRegistrados.find(u => u.email === email && u.password === password);

  if (usuario) {
    document.getElementById("login-container").style.display = "none";
    document.getElementById("dashboard-container").style.display = "block";
    loginError.textContent = "";
  } else {
    loginError.textContent = "Correo o contraseña incorrectos.";
  }
});

// Manejar solicitud de turnos
const turnoForm = document.getElementById("turno-form");
const turnosList = document.getElementById("turnos-list");

turnoForm.addEventListener("submit", (e) => {
  e.preventDefault();
  
  const especialidad = document.getElementById("especialidad").value;
  const profesional = document.getElementById("profesional").value;
  const fecha = document.getElementById("fecha-turno").value;
  const hora = document.getElementById("hora-turno").value;

  const turno = `Especialidad: ${especialidad}, Profesional: ${profesional}, Fecha: ${fecha}, Hora: ${hora}`;
  const li = document.createElement("li");
  li.textContent = turno;
  turnosList.appendChild(li);

  turnoForm.reset();
});

// Cerrar sesión
document.getElementById("logout-button").addEventListener("click", () => {
  document.getElementById("dashboard-container").style.display = "none";
  document.getElementById("login-container").style.display = "block";
  loginForm.reset();
});
