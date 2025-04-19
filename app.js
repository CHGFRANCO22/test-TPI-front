// URL del backend
const BASE_URL = "http://localhost:3000";

// Manejar Login
document.getElementById("login-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch(`${BASE_URL}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const data = await response.json();
            alert("Inicio de sesión exitoso");
            console.log("Usuario:", data);
        } else {
            const error = await response.text();
            alert("Error: " + error);
        }
    } catch (error) {
        console.error("Error al iniciar sesión:", error);
    }
});

// Manejar Registro
document.getElementById("register-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const nombre_completo = document.getElementById("nombre_completo").value;
    const dni = document.getElementById("dni").value;
    const sexo = document.getElementById("sexo").value;
    const email = document.getElementById("email_register").value;
    const password = document.getElementById("password_register").value;

    try {
        const response = await fetch(`${BASE_URL}/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ nombre_completo, dni, sexo, email, password })
        });

        if (response.ok) {
            alert("Registro exitoso");
        } else {
            const error = await response.text();
            alert("Error: " + error);
        }
    } catch (error) {
        console.error("Error al registrar usuario:", error);
    }
});
