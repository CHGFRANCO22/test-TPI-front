// Simular datos de especialidades y profesionales
const especialidades = {
  "Clínica General": ["Dr. Juan Pérez", "Dra. Ana López"],
  "Pediatría": ["Dra. Laura Torres", "Dr. Carlos Gómez"],
  "Cardiología": ["Dr. Mario Fuentes"],
  "Ginecología": ["Dra. Marta Díaz"],
};

// Simular horarios disponibles
let horariosDisponibles = ["08:00", "09:00", "10:00", "11:00", "14:00", "15:00"];

// Cargar especialidades en el menú desplegable
const especialidadSelect = document.getElementById("especialidad");
Object.keys(especialidades).forEach((especialidad) => {
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

  profesionales.forEach((profesional) => {
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
  horariosDisponibles.forEach((horario) => {
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

turnoForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const especialidad = especialidadSelect.value;
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
              "Content-Type": "application/json",
          },
          body: JSON.stringify({
              id_paciente: 1, // Cambiar por el ID del paciente autenticado
              id_especialidad: especialidad,
              id_profesional: profesional,
              fecha: fecha,
              hora: hora,
          }),
      });

      if (response.ok) {
          alert("Turno registrado exitosamente.");
          horariosDisponibles = horariosDisponibles.filter((h) => h !== hora);
          cargarHorarios(); // Actualizar lista de horarios
      } else {
          const error = await response.text();
          alert("Error: " + error);
      }
  } catch (error) {
      console.error("Error al registrar turno:", error);
  }
});

// Cargar servicios del consultorio
async function cargarServicios() {
  const listaServicios = document.getElementById("lista-servicios");

  try {
      const response = await fetch("http://localhost:3000/servicios");
      const servicios = await response.json();

      listaServicios.innerHTML = "";
      servicios.forEach((servicio) => {
          const li = document.createElement("li");
          li.textContent = `${servicio.especialidad}: ${servicio.nombre_completo}`;
          listaServicios.appendChild(li);
      });
  } catch (error) {
      console.error("Error al cargar servicios:", error);
  }
}

// Manejar formulario de contacto
document.getElementById("contacto-form").addEventListener("submit", async (e) => {
  e.preventDefault();

  const nombre = document.getElementById("nombre").value;
  const email = document.getElementById("email").value;
  const mensaje = document.getElementById("mensaje").value;

  try {
      const response = await fetch("http://localhost:3000/contacto", {
          method: "POST",
          headers: {
              "Content-Type": "application/json",
          },
          body: JSON.stringify({ nombre: nombre, email: email, mensaje: mensaje }),
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
cargarHorarios();
cargarServicios();
