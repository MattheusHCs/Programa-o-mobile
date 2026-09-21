# Meu Curso — App Android (Kotlin)

Projeto para o Exercício Prático da Aula 5 (Prof. Leandro Melo).

## Como abrir no Android Studio

1. Abra o Android Studio → **File > Open** → selecione a pasta `MeuCurso`.
2. Aguarde o Gradle sincronizar (primeira sincronização baixa dependências, precisa de internet).
3. Rode em um emulador ou dispositivo físico (botão ▶ Run).

## Estrutura

```
app/src/main/
├── java/com/example/meucurso/
│   ├── MainActivity.kt          Tela 1 - Home
│   ├── SobreCursoActivity.kt    Tela 2 - Sobre o curso
│   └── DisciplinasActivity.kt   Tela 3 - Disciplinas
├── res/layout/
│   ├── activity_main.xml
│   ├── activity_sobre_curso.xml
│   └── activity_disciplinas.xml
├── res/drawable/
│   ├── logo_instituicao.xml     imagem 1 (logo)
│   └── imagem_curso.xml         imagem 2 (banner do curso)
└── res/values/
    ├── strings.xml, colors.xml, styles.xml, themes.xml
```

## Navegação implementada

- Home → "Conheça o curso" → Sobre o Curso (`Intent` + `startActivity`)
- Home → "Disciplinas" → Disciplinas (`Intent` + `startActivity`)
- Sobre o Curso → "Voltar" → `finish()` (retorna à Home)
- Sobre o Curso → "Disciplinas" → Disciplinas
- Disciplinas → "Voltar" → `finish()` (retorna à tela anterior, seja Home ou Sobre)

## Pontos para estudar antes da apresentação

O professor pode pedir para explicar qualquer trecho. Aqui vão os pontos-chave:

- **`Intent`**: objeto que descreve uma ação a ser realizada, aqui usado para dizer ao
  sistema "abra esta outra Activity". `Intent(this, SobreCursoActivity::class.java)` cria
  uma intent explícita apontando para a classe de destino.
- **`startActivity(intent)`**: entrega a intent ao sistema Android, que empilha a nova
  tela sobre a atual (a Activity de origem não é destruída, apenas pausada).
- **`finish()`**: encerra a Activity atual e remove ela da pilha, fazendo o app voltar
  para a tela anterior — é assim que os botões "Voltar" funcionam, sem precisar criar
  uma nova Intent.
- **`findViewById<Button>(R.id...)`**: busca, em tempo de execução, a View que foi
  declarada no XML com aquele `id`, permitindo manipulá-la em Kotlin (ex.: adicionar um
  listener de clique).
- **`setOnClickListener { }`**: registra uma função lambda que é executada quando o
  usuário toca no botão.
- **Cada tela tem seu próprio arquivo XML** em `res/layout/`, associado à Activity
  correspondente via `setContentView(R.layout....)` no `onCreate()`.
- **As imagens** ficam em `res/drawable/` (aqui como vetores XML, editáveis) e são
  referenciadas nos layouts com `android:src="@drawable/nome_do_arquivo"`.
- Se você remover o `setOnClickListener` de um botão, ele continua aparecendo na tela,
  mas deixa de reagir ao toque — nenhuma navegação acontece.

## Personalizando

- Troque o nome da instituição/curso e os textos em `res/values/strings.xml`.
- Substitua `logo_instituicao.xml` e `imagem_curso.xml` por fotos reais (arraste um
  `.png`/`.jpg` para `res/drawable` e atualize o `android:src` nos layouts).
- As disciplinas podem ser editadas em `strings.xml` e nos cards de
  `activity_disciplinas.xml`.
