// Simular datos de especialidades y profesionales
const especialidades = {
  "Clínica General": ["Dr. Juan Pérez", "Dra. Ana López"],
  "Pediatría": ["Dra. Laura Torres", "Dr. Carlos Gómez"],
  "Cardiología": ["Dr. Mario Fuentes"],
  "Ginecología": ["Dra. Marta Díaz"] // Último elemento sin coma adicional
};

// Simular horarios disponibles
let horariosDisponibles = ["08:00", "09:00", "10:00", "11:00", "14:00", "15:00"];

// Cargar especialidades en el menú desplegable
function cargarEspecialidades() {
  const especialidadSelect = document.getElementById("especialidad");
  especialidadSelect.innerHTML = '<option value="" disabled selected>Seleccione una especialidad</option>'; // Limpia opciones previas

  Object.keys(especialidades).forEach(especialidad => {
      const option = document.createElement("option");
      option.value = especialidad;
      option.textContent = especialidad;
      especialidadSelect.appendChild(option);
  });

  // Evento para cargar los profesionales al seleccionar una especialidad
  especialidadSelect.addEventListener("change", cargarProfesionales);
}

// Cargar profesionales según la especialidad seleccionada
function cargarProfesionales() {
  const especialidadSelect = document.getElementById("especialidad");
  const profesionalSelect = document.getElementById("profesional");
  profesionalSelect.innerHTML = '<option value="" disabled selected>Seleccione un profesional</option>'; // Limpia opciones previas

  const profesionales = especialidades[especialidadSelect.value] || [];
  profesionales.forEach(profesional => {
      const option = document.createElement("option");
      option.value = profesional;
      option.textContent = profesional;
      profesionalSelect.appendChild(option);
  });
}

// Cargar horarios disponibles
function cargarHorarios() {
  const horarioSelect = document.getElementById("hora-turno");
  horarioSelect.innerHTML = '<option value="" disabled selected>Seleccione una hora</option>'; // Limpia opciones previas

  horariosDisponibles.forEach(horario => {
      const option = document.createElement("option");
      option.value = horario;
      option.textContent = horario;
      horarioSelect.appendChild(option);
  });
}

// Manejar solicitud de turnos
async function solicitarTurno(event) {
  event.preventDefault();

  const especialidad = document.getElementById("especialidad").value;
  const profesional = document.getElementById("profesional").value;
  const fecha = document.getElementById("fecha-turno").value;
  const hora = document.getElementById("hora-turno").value;

  if (!especialidad || !profesional || !fecha || !hora) {
      alert("Por favor complete todos los campos.");
      return;
  }

  try {
      const response = await fetch("http://localhost:3000/turnos", {
          method: "POST",
          headers: {
              "Content-Type": "application/json"
          },
          body: JSON.stringify({
              id_paciente: 1, // Cambiar por el ID del paciente autenticado
              id_especialidad: especialidad,
              id_profesional: profesional,
              fecha,
              hora
          })
      });

      if (response.ok) {
          alert("Turno registrado exitosamente.");
          // Remueve la hora seleccionada de los horarios disponibles
          horariosDisponibles = horariosDisponibles.filter(h => h !== hora);
          cargarHorarios(); // Actualizar lista de horarios
      } else {
          const error = await response.text();
          alert("Error: " + error);
      }
  } catch (error) {
      console.error("Error al registrar turno:", error);
  }
}

// Manejar formulario de contacto
async function enviarContacto(event) {
  event.preventDefault();

  const nombre = document.getElementById("nombre").value;
  const email = document.getElementById("email").value;
  const mensaje = document.getElementById("mensaje").value;

  if (!nombre || !email || !mensaje) {
      alert("Todos los campos son obligatorios.");
      return;
  }

  try {
      const response = await fetch("http://localhost:3000/contacto", {
          method: "POST",
          headers: {
              "Content-Type": "application/json"
          },
          body: JSON.stringify({ nombre, email, mensaje })
      });

      if (response.ok) {
          alert("Mensaje enviado. Gracias por contactarnos.");
      } else {
          alert("Error al enviar el mensaje.");
      }
  } catch (error) {
      console.error("Error al enviar mensaje de contacto:", error);
  }
}

// Inicializar funcionalidades
function inicializar() {
  cargarEspecialidades();
  cargarHorarios();

  // Manejar eventos de formularios
  document.getElementById("turno-form").addEventListener("submit", solicitarTurno);
  document.getElementById("contacto-form").addEventListener("submit", enviarContacto);
}

// Llamar a la función inicializar al cargar la página
inicializar();
