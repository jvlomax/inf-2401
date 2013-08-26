        .data
.tmp:   .fill   4                       # Temporary storage
        .text
        .globl  main                    
main:   enter   $48,$0                  # Start function main
        movl    $0,%eax                 # 0
        movl    %eax,-48(%ebp)          # max =
        movl    $0,%eax                 # 0
        movl    %eax,-44(%ebp)          # i =
.L0001:                                 # Start for-statement
        movl    -44(%ebp),%eax          # i
        pushl   %eax                    
        movl    $10,%eax                # 10
        popl    %ecx                    
        cmpl    %eax,%ecx               
        movl    $0,%eax                 
        setl    %al                     # Test <
        cmpl    $0,%eax                 
        je      .L0002                  
        movl    -44(%ebp),%eax          # i
        pushl   %eax                    
        call    getint                  # Call getint
        leal    -40(%ebp),%edx          
        popl    %ecx                    
        movl    %eax,(%edx,%ecx,4)      # nums[...] =
        movl    -44(%ebp),%eax          # i
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-44(%ebp)          # i =
        jmp     .L0001                  
.L0002:                                 # End for-statement
        movl    $0,%eax                 # 0
        movl    %eax,-44(%ebp)          # i =
.L0003:                                 # Start for-statement
        movl    -44(%ebp),%eax          # i
        pushl   %eax                    
        movl    $10,%eax                # 10
        popl    %ecx                    
        cmpl    %eax,%ecx               
        movl    $0,%eax                 
        setl    %al                     # Test <
        cmpl    $0,%eax                 
        je      .L0004                  
                                        # Start if-statement
        movl    -44(%ebp),%eax          # i
        leal    -40(%ebp),%edx          # nums[...]
        movl    (%edx,%eax,4),%eax      
        pushl   %eax                    
        movl    -48(%ebp),%eax          # max
        popl    %ecx                    
        cmpl    %eax,%ecx               
        movl    $0,%eax                 
        setg    %al                     # Test >
        cmpl    $0,%eax                 
        je      .L0005                  
        movl    -44(%ebp),%eax          # i
        leal    -40(%ebp),%edx          # nums[...]
        movl    (%edx,%eax,4),%eax      
        movl    %eax,-48(%ebp)          # max =
.L0005:                                 # End if-statement
        movl    -44(%ebp),%eax          # i
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-44(%ebp)          # i =
        jmp     .L0003                  
.L0004:                                 # End for-statement
        movl    -48(%ebp),%eax          # max
        pushl   %eax                    # Push parameter #1
        call    putint                  # Call putint
        addl    $4,%esp                 # Remove parameters
        movl    $10,%eax                # 10
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
.exit$main:
        leave                           
        ret                             # End function main
