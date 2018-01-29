package com.noprestige.kanaquiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeMap;

class KanaQuestionBank extends TreeMap<Integer, KanaQuestion>
{
    private KanaQuestion currentQuestion;
    private String[] fullAnswerList = null;
    private Random rng = new Random();
    private int maxValue = 0;

    private static final int MAX_MULTIPLE_CHOICE_ANSWERS = 6;

    private QuestionRecord previousQuestions = null;

    KanaQuestionBank()
    {
        super();
    }

    void newQuestion() throws NoQuestionsException
    {
        if (this.size() > 1)
        {
            if (previousQuestions == null)
                previousQuestions = new QuestionRecord(Math.min(this.size(), OptionsControl.getInt(R.string.prefid_repetition)));
            do
                currentQuestion = get(floorKey(rng.nextInt(maxValue)));
            while (!previousQuestions.add(currentQuestion));
        }
        else
            throw new NoQuestionsException();
    }

    String getCurrentKana()
    {
        return currentQuestion.getKana();
    }

    boolean checkCurrentAnswer(String response)
    {
        return currentQuestion.checkAnswer(response);
    }

    String fetchCorrectAnswer()
    {
        return currentQuestion.fetchCorrectAnswer();
    }

    boolean addQuestions(KanaQuestion[] questions)
    {
        previousQuestions = null;
        fullAnswerList = null;
        for (KanaQuestion question : questions)
        {
            this.put(maxValue, question);
            maxValue += (int) Math.ceil((1.05f - LogDatabase.DAO.getKanaPercentage(question.getKana())) * 100f);
        }
        return true;
    }

    boolean addQuestions(KanaQuestion[] questions1, KanaQuestion[] questions2)
    {
        return addQuestions(questions1) && addQuestions(questions2);
    }

    boolean addQuestions(KanaQuestionBank questions)
    {
        previousQuestions = null;
        fullAnswerList = null;

        for (Integer key : questions.keySet())
            this.put(maxValue + key, questions.get(key));
        maxValue += questions.maxValue;

        return true;
    }

    String[] getPossibleAnswers()
    {
        return getPossibleAnswers(MAX_MULTIPLE_CHOICE_ANSWERS);
    }

    String[] getPossibleAnswers(int maxChoices)
    {
        if (fullAnswerList == null)
        {
            ArrayList<String> answers = new ArrayList<>();
            for (KanaQuestion question : values())
                if (!answers.contains(question.fetchCorrectAnswer()))
                    answers.add(question.fetchCorrectAnswer());
            fullAnswerList = new String[answers.size()];
            answers.toArray(fullAnswerList);
        }

        //TODO: Weight displayed choices by incorrect answer records

        ArrayList<String> possibleAnswerStrings = new ArrayList<>();

        possibleAnswerStrings.add(fetchCorrectAnswer());

        while (possibleAnswerStrings.size() < Math.min(fullAnswerList.length, maxChoices))
        {
            int rand = rng.nextInt(fullAnswerList.length);
            if (!possibleAnswerStrings.contains(fullAnswerList[rand]))
                possibleAnswerStrings.add(fullAnswerList[rand]);
        }

        Collections.shuffle(possibleAnswerStrings);

        String[] returnValue = new String[possibleAnswerStrings.size()];
        possibleAnswerStrings.toArray(returnValue);
        return returnValue;
    }
}
