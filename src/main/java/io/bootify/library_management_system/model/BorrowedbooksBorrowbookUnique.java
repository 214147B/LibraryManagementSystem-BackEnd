package io.bootify.library_management_system.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import io.bootify.library_management_system.service.BorrowedbooksService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the id value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = BorrowedbooksBorrowbookUnique.BorrowedbooksBorrowbookUniqueValidator.class
)
public @interface BorrowedbooksBorrowbookUnique {

    String message() default "{Exists.borrowedbooks.borrowbook}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class BorrowedbooksBorrowbookUniqueValidator implements ConstraintValidator<BorrowedbooksBorrowbookUnique, Long> {

        private final BorrowedbooksService borrowedbooksService;
        private final HttpServletRequest request;

        public BorrowedbooksBorrowbookUniqueValidator(
                final BorrowedbooksService borrowedbooksService, final HttpServletRequest request) {
            this.borrowedbooksService = borrowedbooksService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Long value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(borrowedbooksService.get(Long.parseLong(currentId)).getBorrowbook())) {
                // value hasn't changed
                return true;
            }
            return !borrowedbooksService.borrowbookExists(value);
        }

    }

}
