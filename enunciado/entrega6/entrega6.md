## Entrega 6 - No-SQL - CassandraDB
Había transcurrido un número considerable de jornadas desde el último arduo cometido encomendado por los malvados investigadores, en aras de continuar con esta titánica prueba que desafiaba a la humanidad, extinguiendo toda esencia humana que pervivía en lo que ahora era una tierra desolada, poblada por autómatas víctimas de un constante acecho viral. La única barrera que nos separaba de tal plaga era la rigurosa reclusión en aquel sombrío recinto de experimentación.

Con parsimonia, desvío mi mirada del último fragmento de código que habíamos logrado implementar junto al equipo, y percibo que la vigilancia de los malévolos científicos sobre nuestra morada se ha disipado. Reuniendo coraje tras unos momentos de reflexión, me alzo de mi asiento y, al abrir la puerta de nuestra lúgubre estancia, permito que una escasa luminosidad se filtre. Contemplo con esbozo la sala principal de aquel laboratorio, antes colmada por una vasta cantidad de profesionales, ahora desierta pero impregnada de la oscura sustancia que se filtraba bajo una de las puertas del recinto hace un tiempo. Aquella entrada, que meses atrás se hallaba herméticamente sellada por un sofisticado sistema de seguridad, ahora se presenta completamente abierta.

Tras explorar durante unos instantes, adentramos en el aposento desde donde solía emerger el científico enfundado en un guante negro, un amplio salón repleto de innovadores experimentos, ahora sumido en la oscuridad y el silencio. Repentinamente, una figura aterrada emerge de las sombras... es el ayudante del científico de guante negro, pero ahora se encuentra en una etapa avanzada de mecanización y le queda poco tiempo. Procede a relatarnos cómo todo se descontroló tras la fuga de una de las criaturas mecanizadas en las que realizaban experimentos, erradicando cualquier vestigio de presencia humana en aquel laboratorio y dando finalización a la cuarentena dentro del recinto.

Ya no queda nada, el aniquilamiento de la humanidad parece ineludible, pero antes de postrar nuestras miradas en el suelo en señal de derrota, el asistente nos muestra el último descubrimiento del científico de guante negro. Hace unos días, hizo un hallazgo sin precedentes y se desencadenó algo extraordinario. En medio de la opresiva atmósfera del laboratorio, un aura envolvió el entorno, revelando un nuevo orden de posibilidades. La magia, hasta entonces solo un concepto fantástico, se tornó tangible y poderosa.

El ayudante nos explica que aún no ha descubierto cómo aprovecharla en nuestro beneficio para poner fin a la mecanización, pero constituye nuestra mejor oportunidad y no nos queda más alternativa que depositar nuestra confianza en este joven enfermo.

Rápidamente, nos muestra una de las investigaciones que ha estado llevando a cabo durante estos días. Nos explica que, si deseamos avanzar y descubrir cómo utilizar la magia en nuestro favor, debemos recopilar y analizar una colosal cantidad de información sobre las infecciones en una vanguardista supercomputadora del laboratorio llamada Cassandra, pero debemos apresurarnos, pues el tiempo apremia. Una vez más, nuevos requisitos y un último esfuerzo, solo unas líneas de código más podrían salvar a la humanidad…


## Requerimientos

Se nos pide realizar un registro en Cassandra que contenga los datos de una infección, ya sea por contagio o infección simple. 
Tener en cuenta que no siempre nos interesan conocer datos como la capacidad de biomecanización del patógeno perteneciente a la especie que infectó al vector, pero se nos explica que esto es importante a veces, por lo que debemos tener que dejar abierta la posibilidad de conocer los datos del patógeno sin utilizar Joins para unir el ID del pátogeno registrado, con sus datos, para esto se debe dividir el trabajo en dos tablas:

## InfeccionSegunEspecie

En esta tabla nos interesa conservar y darle importancia a los datos de la especie que realizó el contagio (nombre, país de origen), además debemos conservar el tipo y el ID del vector infectado.


## InfeccionSegunPatogeno

En esta tabla nos interesa conservar y darle importancia a los datos del patógeno de la especie que realizó el contagio (capacidad de biomecanizacion, capacidad de contagio, tipo), además debemos conservar el tipo y el ID del vector infectado.


## Services

Se requiere implementar los siguientes services:

## InfeccionSegunPatogenoService:

-	agregarReporteDeInfeccion(idVectorInfectado: Long, capacidadDeBiomecanizacion: Int, capacidadDeContagio: Int, tipoDePatogeno: String, tipoDeVectorInfectado: TipoDeVector): InfeccionSegunPatogeno
-	findAll(): List<InfeccionSegunPatogeno>
InfeccionSegunEspecieService:
-	agregarReporteDeInfeccion(idVectorInfectado: Long,nombreEspecie: String, paisOrigenEspecie: String, tipoDeVectorInfectado: TipoDeVector): InfeccionSegunEspecie
-	findAll(): List<InfecciónSegunEspecie>


Con renovada esperanza, nos sumergimos en el desafío de desentrañar los secretos de la magia y utilizarla como arma contra la mecanización implacable. Día y noche, trabajamos incansablemente para recopilar datos, analizar patrones y descubrir las claves que Cassandra nos revelaba. Con cada avance, sentimos la energía mágica crecer a nuestro alrededor, y la fe en un final feliz se afianza en nuestros corazones.

Finalmente, llega el momento crucial. Con el conocimiento adquirido y la magia fluyendo en nuestras venas, nos enfrentamos a la última batalla contra las criaturas mecanizadas. Con conjuros poderosos y habilidades mágicas recién descubiertas, combatimos con determinación y valentía. Los muggles, que habían sido condenados a la extinción, se unen a nuestra lucha, sorprendidos por la existencia de un mundo oculto lleno de maravillas y esperanza.

La magia se revela como un arma formidable, capaz de desmantelar las maquinarias malévolas y devolver a los autómatas a su forma humana original. Con cada hechizo pronunciado, la humanidad se libera de su cautiverio mecánico y recobra su esencia perdida. Los científicos malvados son derrotados, su reinado de terror llega a su fin y la humanidad emerge victoriosa.

En el nuevo amanecer, la magia se convierte en parte integral de nuestro mundo, una fuerza que nos conecta con la naturaleza y nos impulsa a cuidar y proteger nuestro planeta. La convivencia entre muggles y seres mágicos se fortalece, y juntos reconstruimos una sociedad en la que la tecnología y la magia se entrelazan para el bien común.

En esta nueva era, recordamos los días oscuros en los que parecía que la humanidad estaba condenada, pero nos regocijamos en la victoria obtenida a través de la magia. Un final feliz se hace realidad gracias al poder mágico que salvó a la humanidad de la destrucción, demostrando que en los momentos más oscuros, la esperanza y la magia pueden prevalecer y cambiar nuestro destino.






