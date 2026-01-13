package io.github.largeblueberry.jetkit

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory

class SetupAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val projectDir = project.guessProjectDir() ?: return
        val gradleFile = projectDir.findChild("build.gradle.kts") ?: return

        // VirtualFile을 PSI 파일로 변환 (이제 KtFile을 인식할 겁니다!)
        val psiFile = PsiManager.getInstance(project).findFile(gradleFile) as? KtFile ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            val dependenciesBlock = findDependenciesBlock(psiFile)

            if (dependenciesBlock != null) {
                val ktPsiFactory = KtPsiFactory(project)
                // 추가할 코드 (줄바꿈 포함)
                val newDependency = ktPsiFactory.createExpression("implementation(\"com.example:jetkit-library:1.0.0\")")

                // 블록 안에 추가
                dependenciesBlock.addBefore(newDependency, dependenciesBlock.lastChild)
                dependenciesBlock.addBefore(ktPsiFactory.createNewLine(), dependenciesBlock.lastChild)

                Messages.showInfoMessage("성공적으로 삽입되었습니다!", "JetKit")
            } else {
                Messages.showErrorDialog("dependencies 블록을 찾지 못했습니다.", "에러")
            }
        }
    }

    private fun findDependenciesBlock(file: KtFile): KtBlockExpression? {
        val calls = PsiTreeUtil.findChildrenOfType(file, KtCallExpression::class.java)
        val depCall = calls.find { it.calleeExpression?.text == "dependencies" }
        return depCall?.lambdaArguments?.firstOrNull()?.getLambdaExpression()?.bodyExpression
    }
}