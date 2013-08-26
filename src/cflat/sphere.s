        .data
.tmp:   .fill   4                       # Temporary storage
        .globl  pi
pi:     .fill   8                       # double pi;
        .globl  mill
mill:   .fill   8                       # double mill;
        .text
        .globl  surface                 
surface:
        enter   $8,$0                   # Start function surface
        movl    $4,%eax                 # 4
        movl    %eax,.tmp               
        fildl   .tmp                    #   (double)
        fstpl   -8(%ebp)                # four =
        fldl    -8(%ebp)                # four
        subl    $8,%esp                 
        fstpl   (%esp)                  
        fldl    pi                      # pi
        fldl    (%esp)                  
        addl    $8,%esp                 
        fmulp                           # Compute *
        subl    $8,%esp                 
        fstpl   (%esp)                  
        fldl    8(%ebp)                 # r
        fldl    (%esp)                  
        addl    $8,%esp                 
        fmulp                           # Compute *
        subl    $8,%esp                 
        fstpl   (%esp)                  
        fldl    8(%ebp)                 # r
        fldl    (%esp)                  
        addl    $8,%esp                 
        fmulp                           # Compute *
        jmp     .exit$surface           # Return-statement
        fldz                            
.exit$surface:
        leave                           
        ret                             # End function surface
        .globl  volume                  
volume: enter   $24,$0                  # Start function volume
        movl    $10,%eax                # 10
        movl    %eax,.tmp               
        fildl   .tmp                    #   (double)
        fstpl   -24(%ebp)               # ten =
        movl    $40,%eax                # 40
        movl    %eax,.tmp               
        fildl   .tmp                    #   (double)
        fstpl   -8(%ebp)                # fourZero =
        movl    $30,%eax                # 30
        movl    %eax,.tmp               
        fildl   .tmp                    #   (double)
        fstpl   -16(%ebp)               # threeZero =
        fldl    -8(%ebp)                # fourZero
        subl    $8,%esp                 
        fstpl   (%esp)                  
        fldl    -24(%ebp)               # ten
        fldl    (%esp)                  
        addl    $8,%esp                 
        fdivp                           # Compute /
        fstpl   -8(%ebp)                # fourZero =
        fldl    -16(%ebp)               # threeZero
        subl    $8,%esp                 
        fstpl   (%esp)                  
        fldl    -24(%ebp)               # ten
        fldl    (%esp)                  
        addl    $8,%esp                 
        fdivp                           # Compute /
        fstpl   -16(%ebp)               # threeZero =
        fldl    -8(%ebp)                # fourZero
        subl    $8,%esp                 
        fstpl   (%esp)                  
        fldl    -16(%ebp)               # threeZero
        fldl    (%esp)                  
        addl    $8,%esp                 
        fdivp                           # Compute /
        subl    $8,%esp                 
        fstpl   (%esp)                  
        fldl    pi                      # pi
        fldl    (%esp)                  
        addl    $8,%esp                 
        fmulp                           # Compute *
        subl    $8,%esp                 
        fstpl   (%esp)                  
        fldl    8(%ebp)                 # r
        fldl    (%esp)                  
        addl    $8,%esp                 
        fmulp                           # Compute *
        subl    $8,%esp                 
        fstpl   (%esp)                  
        fldl    8(%ebp)                 # r
        fldl    (%esp)                  
        addl    $8,%esp                 
        fmulp                           # Compute *
        subl    $8,%esp                 
        fstpl   (%esp)                  
        fldl    8(%ebp)                 # r
        fldl    (%esp)                  
        addl    $8,%esp                 
        fmulp                           # Compute *
        jmp     .exit$volume            # Return-statement
        fldz                            
.exit$volume:
        leave                           
        ret                             # End function volume
        .globl  main                    
main:   enter   $24,$0                  # Start function main
        movl    $3141593,%eax           # 3141593
        movl    %eax,.tmp               
        fildl   .tmp                    #   (double)
        fstpl   pi                      # pi =
        movl    $1000000,%eax           # 1000000
        movl    %eax,.tmp               
        fildl   .tmp                    #   (double)
        fstpl   mill                    # mill =
        fldl    pi                      # pi
        subl    $8,%esp                 
        fstpl   (%esp)                  
        fldl    mill                    # mill
        fldl    (%esp)                  
        addl    $8,%esp                 
        fdivp                           # Compute /
        fstpl   pi                      # pi =
        movl    $114,%eax               # 114
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
        movl    $63,%eax                # 63
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
        call    getdouble               # Call getdouble
        fstpl   -8(%ebp)                # r =
        movl    $83,%eax                # 83
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
        movl    $58,%eax                # 58
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
        movl    $32,%eax                # 32
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
        fldl    -8(%ebp)                # r
        subl    $8,%esp                 
        fstpl   (%esp)                  # Push parameter #1
        call    surface                 # Call surface
        addl    $8,%esp                 # Remove parameters
        fstpl   -16(%ebp)               # surfaceRes =
        fldl    -8(%ebp)                # r
        subl    $8,%esp                 
        fstpl   (%esp)                  # Push parameter #1
        call    volume                  # Call volume
        addl    $8,%esp                 # Remove parameters
        fstpl   -24(%ebp)               # volumeRes =
        fldl    -16(%ebp)               # surfaceRes
        subl    $8,%esp                 
        fstpl   (%esp)                  # Push parameter #1
        call    putdouble               # Call putdouble
        addl    $8,%esp                 # Remove parameters
        fstps   .tmp                    # Remove return value.
        movl    $32,%eax                # 32
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
        movl    $86,%eax                # 86
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
        movl    $58,%eax                # 58
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
        movl    $32,%eax                # 32
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
        fldl    -24(%ebp)               # volumeRes
        subl    $8,%esp                 
        fstpl   (%esp)                  # Push parameter #1
        call    putdouble               # Call putdouble
        addl    $8,%esp                 # Remove parameters
        fstps   .tmp                    # Remove return value.
        movl    $10,%eax                # 10
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
.exit$main:
        leave                           
        ret                             # End function main
