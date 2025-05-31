import com.example.core.domain.model.RequestStatus

sealed class CreateRequestUiEvent {
    data class Success(val status: RequestStatus) : CreateRequestUiEvent()
}