const apiURL = "http://localhost:8080/api/turnos"; // Cambia la URL según tu backend

// Manejar el formulario para solicitar turnos
document.getElementById("turno-form").addEventListener("submit", function (e) {
  e.preventDefault();

  // Obtener valores del formulario
  const pacienteId = document.getElementById("paciente-id").value;
  const profesionalId = document.getElementById("profesional-id").value;
  const especialidadId = document.getElementById("especialidad-id").value;
  const fechaTurno = document.getElementById("fecha-turno").value;

  const nuevoTurno = {
    pacienteId: pacienteId,
    profesionalId: profesionalId,
    especialidadId: especialidadId,
    fechaTurno: fechaTurno,
  };

  // Enviar datos al backend
  fetch(apiURL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(nuevoTurno),
  })
    .then(response => response.json())
    .then(data => {
      console.log("Turno registrado:", data);
      alert("Turno registrado correctamente");
      cargarTurnos();
    })
    .catch(error => {
      console.error("Error al registrar turno:", error);
      alert("Error al registrar turno");
    });
});

// Cargar y mostrar los turnos
function cargarTurnos() {
  fetch(apiURL)
    .then(response => response.json())
    .then(data => {
      const listaTurnos = document.getElementById("turnos-list");
      listaTurnos.innerHTML = ""; // Limpiar lista
      data.forEach(turno => {
        const item = document.createElement("li");
        item.textContent = `Paciente: ${turno.pacienteId}, Médico: ${turno.profesionalId}, Fecha: ${turno.fechaTurno}`;
        listaTurnos.appendChild(item);
      });
    })
    .catch(error => console.error("Error al cargar turnos:", error));
}

// Cargar turnos al iniciar
cargarTurnos();
