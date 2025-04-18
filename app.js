// Simular datos de especialidades y profesionales
const especialidades = {
  "Clínica General": ["Dr. Juan Pérez", "Dra. Ana López"],
  "Pediatría": ["Dra. Laura Torres", "Dr. Carlos Gómez"],
  "Cardiología": ["Dr. Mario Fuentes"],
  "Ginecología": ["Dra. Marta Díaz"]
};

// Simular horarios disponibles
let horariosDisponibles = ["08:00", "09:00", "10:00", "11:00", "14:00", "15:00"];

// Cargar especialidades en el menú desplegable
const especialidadSelect = document.getElementById("especialidad");
Object.keys(especialidades).forEach(especialidad => {
  const option = document.createElement("option");
  option.value = especialidad;
  option.textContent = especialidad;
  especialidadSelect.appendChild(option);
});

// Cargar profesionales según la especialidad seleccionada
especialidadSelect.addEventListener("change", () => {
  const profesionalSelect = document.getElementById("profesional");
  profesionalSelect.innerHTML = '<option value="" disabled selected>Seleccione un profesional</option>';
  const profesionales = especialidades[especialidadSelect.value];

  profesionales.forEach(profesional => {
    const option = document.createElement("option");
    option.value = profesional;
    option.textContent = profesional;
    profesionalSelect.appendChild(option);
  });
});

// Cargar horarios disponibles
function cargarHorarios() {
  const horarioSelect = document.getElementById("hora-turno");
  horarioSelect.innerHTML = '<option value="" disabled selected>Seleccione una hora</option>';
  horariosDisponibles.forEach(horario => {
    const option = document.createElement("option");
    option.value = horario;
    option.textContent = horario;
    horarioSelect.appendChild(option);
  });
}
cargarHorarios();

// Manejar solicitud de turnos
const turnoForm = document.getElementById("turno-form");
const turnosList = document.getElementById("turnos-list");

turnoForm.addEventListener("submit", (e) => {
  e.preventDefault();

  const especialidad = especialidadSelect.value;
  const profesional = document.getElementById("profesional").value;
  const fecha = document.getElementById("fecha-turno").value;
  const hora = document.getElementById("hora-turno").value;

  // Eliminar el horario elegido de los horarios disponibles
  horariosDisponibles = horariosDisponibles.filter(h => h !== hora);
  cargarHorarios(); // Actualizar lista de horarios

  // Crear el turno y mostrarlo
  const turno = `Especialidad: ${especialidad}, Profesional: ${profesional}, Fecha: ${fecha}, Hora: ${hora}`;
  const li = document.createElement("li");
  li.textContent = turno;
  turnosList.appendChild(li);

  turnoForm.reset();
});
