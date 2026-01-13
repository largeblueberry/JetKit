package io.github.largeblueberry.jetkit

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class SetupAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        // 이 코드가 실행되면 알림창이 뜹니다.
        Messages.showInfoMessage("JetKit 플러그인이 정상 작동합니다!", "성공")
    }
}