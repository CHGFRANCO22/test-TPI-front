const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');
const bcrypt = require('bcrypt'); // Para encriptar contraseñas

const app = express();
app.use(cors());
app.use(express.json()); // Permitir parsing de JSON en las solicitudes

// Conexión a la base de datos MySQL
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '12345', // Cambia por tu contraseña configurada en MySQL
    database: 'salud_total_db'
});

db.connect(err => {
    if (err) {
        console.error('Error conectando a la base de datos:', err);
        return;
    }
    console.log('Conexión a la base de datos exitosa');
});

// Registro de una persona y paciente
app.post('/register', async (req, res) => {
    console.log('Datos recibidos para registro:', req.body);

    const { nombre_completo, dni, sexo, email, password } = req.body;

    if (!nombre_completo || !dni || !sexo || !email || !password) {
        res.status(400).send('Faltan datos necesarios para el registro');
        return;
    }

    try {
        const checkDniQuery = "SELECT * FROM persona WHERE dni = ?";
        db.query(checkDniQuery, [dni], (err, results) => {
            if (err) {
                console.error('Error al verificar el DNI:', err);
                res.status(500).send('Error al verificar el DNI');
                return;
            }

            if (results.length > 0) {
                res.status(400).send('El DNI ya está registrado');
            } else {
                const sqlPersona = "INSERT INTO persona (nombre_completo, dni, sexo) VALUES (?, ?, ?)";
                db.query(sqlPersona, [nombre_completo, dni, sexo], async (err, result) => {
                    if (err) {
                        console.error('Error al registrar persona:', err);
                        res.status(500).send('Error al registrar persona');
                        return;
                    }

                    const id_persona = result.insertId;
                    console.log('Persona registrada con ID:', id_persona);

                    const hashedPassword = await bcrypt.hash(password, 10);

                    const sqlPaciente = "INSERT INTO pacientes (id_persona, email, password) VALUES (?, ?, ?)";
                    db.query(sqlPaciente, [id_persona, email, hashedPassword], (err, result) => {
                        if (err) {
                            console.error('Error al registrar paciente:', err);
                            res.status(500).send('Error al registrar paciente');
                        } else {
                            console.log('Paciente registrado con éxito:', { email });
                            res.send('Paciente registrado exitosamente');
                        }
                    });
                });
            }
        });
    } catch (error) {
        console.error('Error en el proceso de registro:', error);
        res.status(500).send('Error en el servidor');
    }
});

// Inicio de sesión
app.post('/login', async (req, res) => {
    console.log('Datos recibidos para login:', req.body);

    const { email, password } = req.body;

    if (!email || !password) {
        res.status(400).send('Correo y contraseña son requeridos');
        return;
    }

    const sql = "SELECT * FROM pacientes WHERE email = ?";
    db.query(sql, [email], async (err, results) => {
        if (err) {
            console.error('Error al verificar credenciales:', err);
            res.status(500).send('Error en el servidor');
            return;
        }

        if (results.length > 0) {
            const match = await bcrypt.compare(password, results[0].password);
            if (match) {
                console.log('Inicio de sesión exitoso para:', email);
                res.send('Inicio de sesión exitoso');
            } else {
                console.log('Credenciales incorrectas para:', email);
                res.status(401).send('Correo o contraseña incorrectos');
            }
        } else {
            console.log('Correo no registrado:', email);
            res.status(401).send('Correo o contraseña incorrectos');
        }
    });
});

// Obtener servicios y profesionales
app.get('/servicios', (req, res) => {
    const sql = `
        SELECT especialidades.nombre AS especialidad, profesionales.email AS profesional, profesionales.nombre_completo
        FROM especialidades
        LEFT JOIN profesional_especialidad ON especialidades.id = profesional_especialidad.id_especialidad
        LEFT JOIN profesionales ON profesionales.id_profesional = profesional_especialidad.id_profesional;
    `;

    db.query(sql, (err, results) => {
        if (err) {
            console.error('Error al obtener servicios:', err);
            res.status(500).send('Error al obtener servicios');
        } else {
            res.json(results);
        }
    });
});

// Registrar un turno
app.post('/turnos', (req, res) => {
    const { id_paciente, id_especialidad, id_profesional, fecha, hora } = req.body;

    if (!id_paciente || !id_especialidad || !id_profesional || !fecha || !hora) {
        res.status(400).send('Todos los campos son obligatorios para registrar un turno');
        return;
    }

    const sqlCheck = "SELECT * FROM turnos WHERE id_profesional = ? AND fecha_turno = ?";
    db.query(sqlCheck, [id_profesional, `${fecha} ${hora}`], (err, results) => {
        if (err) {
            console.error('Error al verificar disponibilidad:', err);
            res.status(500).send('Error al verificar disponibilidad');
        } else if (results.length > 0) {
            res.status(400).send('La hora ya está reservada. Por favor seleccione otra hora.');
        } else {
            const sqlInsert = `
                INSERT INTO turnos (id_paciente, id_especialidad, id_profesional, fecha_turno, estado)
                VALUES (?, ?, ?, ?, 'En espera');
            `;
            db.query(sqlInsert, [id_paciente, id_especialidad, id_profesional, `${fecha} ${hora}`], (err, result) => {
                if (err) {
                    console.error('Error al registrar turno:', err);
                    res.status(500).send('Error al registrar turno');
                } else {
                    res.send('Turno registrado exitosamente');
                }
            });
        }
    });
});

// Consultar turnos
app.get('/turnos', (req, res) => {
    const { id_paciente } = req.query;

    if (!id_paciente) {
        res.status(400).send('Se requiere el ID del paciente para consultar sus turnos');
        return;
    }

    const sql = `
        SELECT turnos.id AS turno_id, especialidades.nombre AS especialidad, profesionales.nombre_completo AS profesional,
               turnos.fecha_turno, turnos.estado
        FROM turnos
        INNER JOIN especialidades ON turnos.id_especialidad = especialidades.id
        INNER JOIN profesionales ON turnos.id_profesional = profesionales.id_profesional
        WHERE turnos.id_paciente = ?;
    `;

    db.query(sql, [id_paciente], (err, results) => {
        if (err) {
            console.error('Error al consultar turnos:', err);
            res.status(500).send('Error al consultar turnos');
        } else {
            res.json(results);
        }
    });
});

// Área de contacto
app.post('/contacto', (req, res) => {
    const { nombre, email, mensaje } = req.body;

    if (!nombre || !email || !mensaje) {
        res.status(400).send('Todos los campos son obligatorios para enviar una consulta');
        return;
    }

    console.log('Mensaje recibido de contacto:', { nombre, email, mensaje });
    res.send('Gracias por tu consulta. Nos pondremos en contacto contigo pronto.');
});

// Iniciar servidor
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
